package com.minburg.movingtool.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ceylonlabs.imageviewpopup.ImagePopup;
import com.minburg.movingtool.R;
import com.minburg.movingtool.models.PersonalItem;
import com.minburg.movingtool.ui.main.EditPersonalItemFragment;
import com.minburg.movingtool.ui.main.MainFragment;
import com.minburg.movingtool.ui.main.NewPersonalItemFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {


    //    private MainViewModel mViewModel;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private List<PersonalItem> filteredList = new ArrayList<>();
    private List<PersonalItem> allPersonalItems = new ArrayList<>();
    private Context context;
    private MainFragment fragment;
    private String currentFilter = "";

    public MainRecyclerAdapter(Context context, MainFragment fragment) {
        this.context = context;
        this.fragment = fragment;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_item, null);
        return new MyViewHolderItem(view);

//        if (viewType == TYPE_ITEM) {
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_item, null);
//            return new MyViewHolderItem(view);
//        } else if (viewType == TYPE_HEADER) {
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_header, null);
//            return new MyViewHolderHeader(view);
//
//        }
//        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {


//        if (holder instanceof MyViewHolderItem) {
        ((MyViewHolderItem) holder).textName.setText(getItemAt(position).getName());
        ((MyViewHolderItem) holder).textName.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                fragment.editItemFragment(getItemAt(position));
                return true;
            }
        });
        if (getItemAt(position).getEstimatedValue() == 0) {
            ((MyViewHolderItem) holder).textValue.setText("");
        } else {
            ((MyViewHolderItem) holder).textValue.setText(String.valueOf(getItemAt(position).getEstimatedValue()));
        }
        ((MyViewHolderItem) holder).textCategory.setText(getItemAt(position).getCategory().toString());
        ((MyViewHolderItem) holder).textOwnership.setText(getItemAt(position).getOwnership().convertToString());


        File file = new File(getItemAt(position).getLocalStorage());
        if (file.exists()) {
            Glide
                    .with(((MyViewHolderItem) holder).thumbnail.getContext())
                    .load(file)
                    .apply(RequestOptions.circleCropTransform())
                    .placeholder(R.drawable.all_circle_white_bg)
                    .into(((MyViewHolderItem) holder).thumbnail);
        }
        ImagePopup imagePopup = new ImagePopup(context);
        imagePopup.setWindowHeight(800); // Optional
        imagePopup.setWindowWidth(800); // Optional
        imagePopup.setBackgroundColor(Color.BLACK);  // Optional
        imagePopup.setFullScreen(true); // Optional
        imagePopup.setHideCloseIcon(true);  // Optional
        imagePopup.setImageOnClickClose(true);  // Optional


        imagePopup.initiatePopupWithGlide(file.getAbsolutePath()); // Load Image from Drawable


        ((MyViewHolderItem) holder).thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /** Initiate Popup view **/
                imagePopup.viewPopup();

            }
        });


        ((MyViewHolderItem) holder).deleteItem.setOnLongClickListener(v -> {
            ((MainFragment) fragment).deleteItem(getItemAt(position));
//                super.notifyDataSetChanged();
            return false;
        });
//        }

//        else if (holder instanceof MyViewHolderHeader) {
//            ((MyViewHolderHeader) holder).sortName.setOnClickListener(v -> {
//                ((MainFragment)fragment).sortByName();
//            });
//
//            ((MyViewHolderHeader) holder).sortValue.setOnClickListener(v -> {
//                ((MainFragment)fragment).sortByValue();
//            });
//
//            ((MyViewHolderHeader) holder).sortCategory.setOnClickListener(v -> {
//                ((MainFragment)fragment).sortByCategory();
//            });
//
//            ((MyViewHolderHeader) holder).sortOwnership.setOnClickListener(v -> {
//                ((MainFragment)fragment).sortByOwnership();
//            });
//        }
    }


    private PersonalItem getItemAt(int position) {
        return filteredList.get(position);
//        return getItem(position - 1);
    }

//    @Override
//    public int getItemViewType(int position) {
//        if (isPositionHeader(position))
//            return TYPE_HEADER;
//
//        return TYPE_ITEM;
//    }

//    private boolean isPositionHeader(int position) {
////        return position == 0;
//        return false;
//    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public void setItems(List<PersonalItem> personalItems) {
        this.allPersonalItems = personalItems;
        exampleFilter.filter(currentFilter);
//        this.filteredList = new ArrayList<>(personalItems);
        notifyDataSetChanged();
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<PersonalItem> tempFilteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                tempFilteredList.addAll(allPersonalItems);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (PersonalItem p : allPersonalItems) {
                    if (p.getName().toLowerCase().trim().contains(filterPattern)
                            || p.getCategory().toString().toLowerCase().trim().contains(filterPattern)) {
                        tempFilteredList.add(p);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = tempFilteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredList.clear();
            filteredList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    public void setCurrentFilter(String currentFilter) {
        this.currentFilter = currentFilter;
    }

    public class MyViewHolderItem extends RecyclerView.ViewHolder {

        TextView textName;
        TextView textValue;
        TextView textCategory;
        TextView textOwnership;
        ImageView thumbnail;
        ImageView deleteItem;
        ProgressBar progressBar;

        public MyViewHolderItem(View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textName);
            textValue = itemView.findViewById(R.id.textValue);
            textCategory = itemView.findViewById(R.id.textCategory);
            textOwnership = itemView.findViewById(R.id.textOwnership);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            deleteItem = itemView.findViewById(R.id.deleteItem);
            deleteItem.setVisibility(View.VISIBLE);
            progressBar = itemView.findViewById(R.id.progressbar);
        }
    }

//    public class MyViewHolderHeader extends RecyclerView.ViewHolder {
//
//        TextView sortName;
//        TextView sortValue;
//        TextView sortCategory;
//        TextView sortOwnership;
//
//        public MyViewHolderHeader(View itemView) {
//            super(itemView);
//            sortName = itemView.findViewById(R.id.sortName);
//            sortName.setText("Name");
//            sortValue = itemView.findViewById(R.id.sortValue);
//            sortValue.setText("Value");
//            sortCategory = itemView.findViewById(R.id.sortCategory);
//            sortCategory.setText("Category");
//            sortOwnership = itemView.findViewById(R.id.sortOwnership);
//            sortOwnership.setText("?");
//        }
//    }
}
