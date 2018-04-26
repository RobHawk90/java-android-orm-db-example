package robhawk.com.br.orm_example.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import java.util.LinkedList;
import java.util.List;

import robhawk.com.br.orm_example.R;
import robhawk.com.br.orm_example.data.model.Task;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private List<Task> dataSet;
    private OnItemClick onComplete;
    private OnItemClick onRemove;
    private OnItemClick onDetail;

    public TaskAdapter() {
        this.dataSet = new LinkedList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_task, parent, false);
        return new ViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Task task = dataSet.get(position);
        holder.checkbox.setText(task.description);
        holder.checkbox.setChecked(task.completed);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public void add(Task task) {
        dataSet.add(task);
        notifyDataSetChanged();
    }

    public Task get(int position) {
        return dataSet.get(position);
    }

    public void setOnComplete(OnItemClick onComplete) {
        this.onComplete = onComplete;
    }

    public void setOnRemove(OnItemClick onRemove) {
        this.onRemove = onRemove;
    }

    public void setOnDetail(OnItemClick onDetail) {
        this.onDetail = onDetail;
    }

    public void remove(Task task) {
        dataSet.remove(task);
        notifyDataSetChanged();
    }

    public void setDataSet(@NonNull List<Task> dataSet) {
        this.dataSet = dataSet;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        public final CheckBox checkbox;

        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnCreateContextMenuListener(this);
            checkbox = itemView.findViewById(R.id.adapter_task_checkbox);
            checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    onComplete.click(get(position), position, view);
                }
            });
        }

        @Override
        public void onCreateContextMenu(final ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
            final int position = getAdapterPosition();
            final Task task = get(position);

            MenuItem remove = menu.add(R.string.remove);
            remove.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    onRemove.click(task, position, menuItem.getActionView());
                    return false;
                }
            });

            MenuItem detail = menu.add(R.string.detail);
            detail.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    onDetail.click(task, position, menuItem.getActionView());
                    return false;
                }
            });
        }
    }

    interface OnItemClick {
        void click(Task task, int position, View view);
    }
}
