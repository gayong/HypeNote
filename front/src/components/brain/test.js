"use client";

import * as d3 from "d3";
import { select, selectAll } from "d3";
import { mean, median } from "d3-array";
// import { howto } from "@d3/example-components";
// import { Swatches } from "@d3/color-legend";
import { useEffect, useRef, useState } from "react";
import { useRouter } from "next/navigation";
import { useAllDiagram } from "@/hooks/useAllDiagram";

const ThreeScene = () => {
  const router = useRouter();
  const ref = useRef();
  const { data: response, isLoading, error } = useAllDiagram();
  const [nodes, setNodes] = useState([]);
  const [links, setLinks] = useState([]);

  useEffect(() => {
    if (response) {
      console.log("날 뇌에 담아줘!", response.data.result);
      console.log("노드", response.data.result.nodes);
      setNodes(response.data.result.nodes);
      console.log("링크", response.data.result.links);
      setLinks(response.data.result.links);
    }
  }, [response]);

  // const nodes = [
  //   { id: "네트워크", group: 1 },
  //   { id: "운영체제", group: 1 },
  //   { id: "프레임워크", group: 2 },
  //   { id: "자료구조", group: 2 },
  //   { id: "node5", group: 2 },
  //   { id: "node6", group: 3 },
  //   { id: "node7", group: 3 },
  //   { id: "node8", group: 4 },
  //   { id: "node9", group: 4 },
  //   { id: "node10", group: 4 },
  // ];

  // const links = [
  //   { source: "네트워크", target: "운영체제" },
  //   { source: "운영체제", target: "프레임워크" },
  //   { source: "네트워크", target: "자료구조" },
  //   { source: "네트워크", target: "node7" },
  //   { source: "자료구조", target: "node5" },
  //   { source: "node5", target: "node6" },
  //   { source: "자료구조", target: "node6" },
  //   { source: "node8", target: "node9" },
  //   { source: "node8", target: "node10" },
  // ];

  useEffect(() => {
    console.log("zz", nodes, links);
    function ForceGraph(
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
        nodeStrength = -50, // 노드끼리 밀어내는 힘, 절댓값 클수록 많이 밀어냄
        linkSource = ({ source }) => source, // given d in links, returns a node identifier string
        linkTarget = ({ target }) => target, // given d in links, returns a node identifier string
        linkStroke = "#999", // link stroke color
        linkStrokeOpacity = 0.6, // link stroke opacity
        linkStrokeWidth = 1.5, // given d in links, returns a stroke width in pixels
        linkStrokeLinecap = "round", // link stroke linecap
        linkStrength,
        // colors = d3.schemeTableau10, // an array of color strings, for the node groups
        width = window.innerWidth, // outer width, in pixels
        height = window.innerHeight, // outer height, in pixels
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
      const forceNode = d3.forceManyBody();
      const forceLink = d3
        .forceLink(links)
        .id((d) => d.id)
        .distance(100);
      if (nodeStrength !== undefined) forceNode.strength(nodeStrength);
      if (linkStrength !== undefined) forceLink.strength(linkStrength);

      const simulation = d3
        .forceSimulation(nodes)
        .force("link", forceLink)
        .force("charge", forceNode)
        .force("center", d3.forceCenter().strength(0.02))
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
        .on("click", function (d) {
          router.push(`/editor/${d.editorId}`);
          // router.push("/editor/1");
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
          if (!event.active) simulation.alphaTarget(0.3).restart();
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
    }

    if (nodes.length > 0 && links.length > 0) {
      ForceGraph({ nodes, links });
    }
  }, [nodes, links]);

  // return <div ref={ref} />;
  return <div ref={ref} style={{ width: "100%", height: "100%" }} />;
};

export default ThreeScene;
