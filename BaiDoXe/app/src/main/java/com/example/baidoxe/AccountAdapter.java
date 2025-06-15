package com.example.baidoxe;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.AccountViewHolder> {

    private List<Account> accountList;
    private OnLockClickListener lockClickListener;

    public interface OnLockClickListener {
        void onLockClick(String email, boolean isLocked);
    }

    public AccountAdapter(List<Account> accountList, OnLockClickListener lockClickListener) {
        this.accountList = accountList;
        this.lockClickListener = lockClickListener;
    }

    @NonNull
    @Override
    public AccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_account, parent, false);
        return new AccountViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountViewHolder holder, int position) {
        Account account = accountList.get(position);
        holder.tvEmail.setText(account.getEmail());
        boolean isLocked = account.isLocked();
        holder.btnLock.setText(isLocked ? "Mở khóa" : "Khóa");
        holder.btnLock.setOnClickListener(v -> lockClickListener.onLockClick(account.getEmail(), isLocked));
    }

    @Override
    public int getItemCount() {
        return accountList.size();
    }

    static class AccountViewHolder extends RecyclerView.ViewHolder {
        TextView tvEmail;
        Button btnLock;

        AccountViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEmail = itemView.findViewById(R.id.tv_email);
            btnLock = itemView.findViewById(R.id.btn_lock);
        }
    }
}