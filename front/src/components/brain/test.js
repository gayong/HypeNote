"use client";

import * as d3 from "d3";
import { select, selectAll } from "d3";
import { mean, median } from "d3-array";

import { useEffect, useRef, useState, useCallback } from "react";
import { useRouter } from "next/navigation";
import { useAllDiagram } from "@/hooks/useAllDiagram";

import SelectShare from "./SelectShare";
import useGetUserInfo from "@/hooks/useGetUserInfo";

const ThreeScene = () => {
  const router = useRouter();
  const ref = useRef();
  const { data: response, isLoading, error } = useAllDiagram();
  const [nodes, setNodes] = useState([]);
  const [links, setLinks] = useState([]);
  const [myNodes, setMyNodes] = useState([]);
  const [myLinks, setMyLinks] = useState([]);
  const [shareNodes, setShareNodes] = useState([]);
  const [shareLinks, setShareLinks] = useState([]);
  const { data: user } = useGetUserInfo();
  // const [innerWidth, setInnerWidth] = useState(window.innerWidth - 350);
  // const [innerHeight, setInnerHeight] = useState(window.innerHeight - 90);

  const [prevResponse, setPrevResponse] = useState(null);

  useEffect(() => {
    if (response && user) {
      if (!prevResponse || JSON.stringify(prevResponse) !== JSON.stringify(response)) {
        console.log("날 뇌에 담아줘!", response.data.result);
        setNodes(response.data.result.nodes);
        setMyNodes(response.data.result.nodes);
        setMyLinks(response.data.result.links);
        setLinks(response.data.result.links);
        setPrevResponse(response);
      }
    }
  }, [response]);

  const [prevSharedData, setPrevSharedData] = useState(null);

  const handleReceive = (sharedData) => {
    if (!sharedData) {
      if (
        JSON.stringify(myNodes) !== JSON.stringify(shareNodes) ||
        JSON.stringify(myLinks) !== JSON.stringify(shareLinks)
      ) {
        setShareNodes([...myNodes]);
        setShareLinks([...myLinks]);
      }
    } else {
      if (
        !prevSharedData ||
        JSON.stringify(sharedData.nodes) !== JSON.stringify(prevSharedData.nodes) ||
        JSON.stringify(sharedData.links) !== JSON.stringify(prevSharedData.links)
      ) {
        setShareNodes([...sharedData.nodes]);
        setShareLinks([...sharedData.links]);
        setNodes([...sharedData.nodes]);
        setLinks([...sharedData.links]);
        setPrevSharedData(sharedData);
      }
    }
  };

  const ForceGraph = useCallback(function (
    { nodes, links },
    {
      nodeId = (d) => d.id, // given d in nodes, returns a unique identifier (string)
      nodeGroup, // given d in nodes, returns an (ordinal) value for color
      nodeGroups, // an array of ordinal values representing the node groups
      nodeTitle, // given d in nodes, a title string
      nodeFill = "currentColor", // node stroke fill (if not using a group color encoding)
      nodeStroke = "#bab8b8", // node stroke color
      nodeStrokeWidth = 2.5, // 노드 테두리 굵기
      nodeStrokeOpacity = 1, // node stroke opacity
      nodeRadius = 10, // node radius, in pixels
      nodeStrength = -120, // 노드끼리 밀어내는 힘, 절댓값 클수록 많이 밀어냄
      linkSource = ({ source }) => source, // given d in links, returns a node identifier string
      linkTarget = ({ target }) => target, // given d in links, returns a node identifier string
      linkStroke = "#999", // link stroke color
      linkStrokeOpacity = 0.6, // link stroke opacity
      linkStrokeWidth = (d) => d.similarity * 2, // given d in links, returns a stroke width in pixels
      linkStrokeLinecap = "round", // link stroke linecap
      linkStrength,
      // colors = d3.schemeTableau10, // an array of color strings, for the node groups
      // width = window.innerWidth - 350, // outer width, in pixels
      // height = window.innerHeight - 90, // outer height, in pixels
      width = 1200, // outer width, in pixels
      height = 700, // outer height, in pixels
      invalidation, // when this promise resolves, stop the simulation
    } = {}
  ) {
    // Compute values.
    const N = d3.map(nodes, nodeId).map(intern);
    const LS = d3.map(links, linkSource).map(intern);
    const LT = d3.map(links, linkTarget).map(intern);
    if (nodeTitle === undefined) nodeTitle = (_, i) => N[i];
    const T = nodeTitle == null ? null : d3.map(nodes, nodeTitle);
    const G = nodeGroup == null ? null : d3.map(nodes, nodeGroup).map(intern);
    const W = typeof linkStrokeWidth !== "function" ? null : d3.map(links, linkStrokeWidth);
    const L = typeof linkStroke !== "function" ? null : d3.map(links, linkStroke);

    // Replace the input nodes and links with mutable objects for the simulation.
    // x, y 위치
    // nodes = d3.map(nodes, (_, i) => ({ id: N[i], group: nodes[i[1]] }));
    // links = d3.map(links, (_, i) => ({ source: LS[i], target: LT[i] }));

    // Compute default domains.
    // if (G && nodeGroups === undefined) nodeGroups = d3.sort(G);

    // Construct the scales.
    // const color = nodeGroup == null ? null : d3.scaleOrdinal(nodeGroups, colors);
    const color = d3.scaleOrdinal(d3.schemeCategory10);

    // Construct the forces.
    const forceNode = d3.forceManyBody().strength(-120).distanceMax(100);
    const forceLink = d3
      .forceLink(links)
      .id((d) => d.id)
      .distance(150);
    // if (nodeStrength !== undefined) forceNode.strength(nodeStrength);
    if (linkStrength !== undefined) forceLink.strength(linkStrength);

    const simulation = d3
      .forceSimulation(nodes)
      .force("link", forceLink)
      .force("charge", forceNode)
      .force("center", d3.forceCenter().strength(0.015))
      .on("tick", ticked);

    const svg = d3
      .select(ref.current)
      .append("svg")
      .attr("width", width)
      .attr("height", height)
      .attr("viewBox", [-width / 2, -height / 2, width, height])
      .attr("style", "overflow: hidden;");

    const link = svg
      .append("g")
      .attr("stroke", typeof linkStroke !== "function" ? linkStroke : null)
      .attr("stroke-opacity", linkStrokeOpacity)
      .attr("stroke-width", typeof linkStrokeWidth !== "function" ? linkStrokeWidth : null)
      .attr("stroke-linecap", linkStrokeLinecap)
      .selectAll("line")
      .data(links)
      .join("line");

    const node = svg
      .append("g")
      .attr("stroke", nodeStroke)
      .attr("stroke-opacity", nodeStrokeOpacity)
      // .attr("stroke-width", nodeStrokeWidth)
      .selectAll("g")
      .data(nodes)
      .join("g")
      .call(drag(simulation));

    node
      .append("circle")
      .attr("r", nodeRadius)
      .attr("fill", (d) => color(d.group))
      .attr("stroke-width", nodeStrokeWidth)
      .on("click", function (ev) {
        if (ev.target.__data__) {
          const data = ev.target.__data__;
          console.log(data);
          router.push(`/editor/${data.editorId}`);
        }
      });

    node
      .append("text")
      .text((d) => d.title)
      .attr("x", 0)
      .attr("y", 28)
      .attr("text-anchor", "middle")
      .style("font-family", "preLt");

    if (W) link.attr("stroke-width", ({ index: i }) => W[i]);
    if (L) link.attr("stroke", ({ index: i }) => L[i]);
    // if (G) node.attr("fill", ({ index: i }) => color(G[i]));
    if (T) node.append("title").text(({ index: i }) => T[i]);
    if (invalidation != null) invalidation.then(() => simulation.stop());

    function intern(value) {
      return value !== null && typeof value === "object" ? value.valueOf() : value;
    }

    function ticked() {
      link
        .attr("x1", (d) => d.source.x)
        .attr("y1", (d) => d.source.y)
        .attr("x2", (d) => d.target.x)
        .attr("y2", (d) => d.target.y);

      node.attr("transform", (d) => `translate(${d.x}, ${d.y})`);
    }

    function drag(simulation) {
      function dragstarted(event) {
        if (!event.active) simulation.alphaTarget(0.2).restart();
        event.subject.fx = event.subject.x;
        event.subject.fy = event.subject.y;
      }

      function dragged(event) {
        event.subject.fx = event.x;
        event.subject.fy = event.y;
      }

      function dragended(event) {
        if (!event.active) simulation.alphaTarget(0);
        event.subject.fx = null;
        event.subject.fy = null;
      }

      svg.call(
        d3
          .zoom()
          .scaleExtent([0.75, 1.5])
          .on("zoom", function (event) {
            let dx = Math.min(0, Math.max(event.transform.x, width - width * event.transform.k));
            let dy = Math.min(0, Math.max(event.transform.y, height - height * event.transform.k));

            svg.attr("transform", `translate(${dx}, ${dy}) scale(${event.transform.k})`);
          })
      );

      const zoom = d3
        .zoom()
        .scaleExtent([1, 1.5])
        .on("zoom", function (event) {
          let dx = Math.min(0, Math.max(event.transform.x, width - width * event.transform.k));
          let dy = Math.min(0, Math.max(event.transform.y, height - height * event.transform.k));
          svg.attr("transform", `translate(${dx}, ${dy}) scale(${event.transform.k})`);
        });

      // SVG에 Zoom 설정 적용
      svg.call(zoom);

      // 초기 배율
      svg.call(zoom.scaleTo, 1.1);

      return d3.drag().on("start", dragstarted).on("drag", dragged).on("end", dragended);
    }
  }, []);

  useEffect(() => {
    // 기존 그래프 삭제
    d3.select(ref.current).selectAll("svg").remove();
    // 새로운 force simulation 생성
    const simulation = d3
      .forceSimulation(nodes)
      .force(
        "link",
        d3
          .forceLink(links)
          .id((d) => d.id)
          .distance(150)
      )
      .force("charge", d3.forceManyBody().strength(-120).distanceMax(140))
      .force("center", d3.forceCenter().strength(0.015));
    // 그래프 그리기
    if (shareNodes.length > 0) {
      ForceGraph({ nodes: [...shareNodes], links: [...shareLinks] });
    } else if (nodes.length > 0) {
      ForceGraph({ nodes: [...nodes], links: [...links] });
    }
  }, [shareNodes, shareLinks, nodes, links]);

  return (
    <div ref={ref} style={{ width: "100%", height: "100%" }}>
      <SelectShare onReceive={handleReceive} />
    </div>
  );
};

export default ThreeScene;
