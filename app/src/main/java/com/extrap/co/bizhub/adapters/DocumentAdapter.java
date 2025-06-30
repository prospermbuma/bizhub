package com.extrap.co.bizhub.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.extrap.co.bizhub.R;
import com.extrap.co.bizhub.data.entities.Document;

import java.util.ArrayList;
import java.util.List;

public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.DocumentViewHolder> {
    
    private List<Document> documents;
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;
    
    public interface OnItemClickListener {
        void onItemClick(Document document);
    }
    
    public interface OnItemLongClickListener {
        boolean onItemLongClick(Document document);
    }
    
    public DocumentAdapter() {
        this.documents = new ArrayList<>();
    }
    
    public void setDocuments(List<Document> documents) {
        this.documents = documents;
        notifyDataSetChanged();
    }
    
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    
    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.onItemLongClickListener = listener;
    }
    
    @NonNull
    @Override
    public DocumentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_document, parent, false);
        return new DocumentViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull DocumentViewHolder holder, int position) {
        Document document = documents.get(position);
        holder.bind(document);
    }
    
    @Override
    public int getItemCount() {
        return documents.size();
    }
    
    class DocumentViewHolder extends RecyclerView.ViewHolder {
        
        private ImageView documentImage;
        private TextView documentName;
        private TextView documentSize;
        private TextView documentDate;
        
        public DocumentViewHolder(@NonNull View itemView) {
            super(itemView);
            
            documentImage = itemView.findViewById(R.id.document_image);
            documentName = itemView.findViewById(R.id.document_name);
            documentSize = itemView.findViewById(R.id.document_size);
            documentDate = itemView.findViewById(R.id.document_date);
            
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && onItemClickListener != null) {
                    onItemClickListener.onItemClick(documents.get(position));
                }
            });
            
            itemView.setOnLongClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && onItemLongClickListener != null) {
                    return onItemLongClickListener.onItemLongClick(documents.get(position));
                }
                return false;
            });
        }
        
        public void bind(Document document) {
            documentName.setText(document.getFileName());
            documentSize.setText(document.getFileSizeFormatted());
            
            // Format date
            java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("MMM dd, yyyy", java.util.Locale.getDefault());
            documentDate.setText(dateFormat.format(new java.util.Date(document.getCreatedAt())));
            
            // Set document type icon
            if (document.isImage()) {
                documentImage.setImageResource(R.drawable.ic_image);
            } else if (document.isPdf()) {
                documentImage.setImageResource(R.drawable.ic_pdf);
            } else if (document.isVideo()) {
                documentImage.setImageResource(R.drawable.ic_video);
            } else {
                documentImage.setImageResource(R.drawable.ic_document);
            }
        }
    }
} 