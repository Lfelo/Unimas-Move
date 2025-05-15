package com.example.unimasmove;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends BaseActivity {
    private RecyclerView rvChatMessages;
    private EditText etMessage;
    private Button btnSend;
    private ChatAdapter chatAdapter;
    private List<ChatMessage> chatMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Initialize views
        rvChatMessages = findViewById(R.id.rvChatMessages);
        etMessage = findViewById(R.id.etMessage);
        btnSend = findViewById(R.id.btnSend);

        // Setup RecyclerView
        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(chatMessages);
        rvChatMessages.setLayoutManager(new LinearLayoutManager(this));
        rvChatMessages.setAdapter(chatAdapter);

        // Add some sample messages
        chatMessages.add(new ChatMessage("Hello, how can I help you?", "Driver", true));
        chatAdapter.notifyItemInserted(0);

        // Set click listeners
        btnSend.setOnClickListener(v -> sendMessage());
    }

    private void sendMessage() {
        String message = etMessage.getText().toString().trim();
        if (!message.isEmpty()) {
            chatMessages.add(new ChatMessage(message, "You", false));
            chatAdapter.notifyItemInserted(chatMessages.size() - 1);
            etMessage.setText("");
            rvChatMessages.scrollToPosition(chatMessages.size() - 1);

            // Simulate driver reply after 1 second
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                chatMessages.add(new ChatMessage("Thanks for your message!", "Driver", true));
                chatAdapter.notifyItemInserted(chatMessages.size() - 1);
                rvChatMessages.scrollToPosition(chatMessages.size() - 1);
            }, 1000);
        }
    }
}