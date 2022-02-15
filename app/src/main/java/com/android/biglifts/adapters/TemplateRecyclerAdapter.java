package com.android.biglifts.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.biglifts.R;
import com.android.biglifts.models.TemplateModel;

import java.util.ArrayList;

public class TemplateRecyclerAdapter extends RecyclerView.Adapter<TemplateRecyclerAdapter.ViewHolder> {

    // Constants
    private static final String TAG = "TemplateRecyclerAdapter";

    // Variables
    private Context mContext;
    private OnTemplateListener mOnTemplateListener;
    private ArrayList<TemplateModel> mTemplatesList;

    public TemplateRecyclerAdapter(Context context, ArrayList<TemplateModel> templateList, OnTemplateListener onTemplateListener) {
        mContext = context;
        mTemplatesList = templateList;
        mOnTemplateListener = onTemplateListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_template, parent, false);
        return new ViewHolder(view, mOnTemplateListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TemplateRecyclerAdapter.ViewHolder holder, int position) {
        TemplateModel template = mTemplatesList.get(position);

        holder.itemView.setTag(template.getId());
        holder.tv_templateName.setText(template.getTemplateName());
        holder.tv_exercisesInTemplate.setText("temp");
    }

    @Override
    public int getItemCount() {
        return mTemplatesList.size();
    }

    public interface OnTemplateListener {
        void onTemplateClick(int position, View view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_templateName, tv_exercisesInTemplate;
        ImageView iv_options;

        OnTemplateListener onTemplateListener;

        public ViewHolder(View itemView, OnTemplateListener onTemplateListener) {
            super(itemView);
            tv_templateName = itemView.findViewById(R.id.row_template_tv_templateName);
            tv_exercisesInTemplate = itemView.findViewById(R.id.row_template_tv_exercisesInTemplate);
            iv_options = itemView.findViewById(R.id.row_template_iv_options);

            this.onTemplateListener = onTemplateListener;

            iv_options.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onTemplateListener.onTemplateClick(getBindingAdapterPosition(), view);
        }
    }

}
