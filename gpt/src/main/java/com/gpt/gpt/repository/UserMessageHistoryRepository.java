package com.gpt.gpt.repository;

import com.gpt.gpt.domain.MessageHistory;

public interface UserMessageHistoryRepository {
    void saveMessageHistory(MessageHistory messageHistory);

}
