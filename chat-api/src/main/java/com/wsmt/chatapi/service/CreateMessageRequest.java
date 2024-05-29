package com.wsmt.chatapi.service;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateMessageRequest {
    private String sender;
    private String text;
    private String session;
}
