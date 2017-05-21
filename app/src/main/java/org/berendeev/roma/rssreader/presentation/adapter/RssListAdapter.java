package org.berendeev.roma.rssreader.presentation.adapter;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.berendeev.roma.rssreader.R;
import org.berendeev.roma.rssreader.Util;
import org.berendeev.roma.rssreader.domain.entity.RssItem;
import org.berendeev.roma.rssreader.presentation.controller.RssListController;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RssListAdapter extends RecyclerView.Adapter<RssListAdapter.RssViewHolder> {

    private final Context context;
    private List<RssItem> rssItems;
    private RssListController controller;

    public RssListAdapter(List<RssItem> rssItems, RssListController controller, Context context) {
        this.rssItems = rssItems;
        this.controller = controller;
        this.context = context;
        hasStableIds();
    }

    @Override public long getItemId(int position) {
        return rssItems.get(position).hashCode();
    }

    @Override public RssViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rss_record_item, parent, false);
        return new RssViewHolder(view);
    }

    @Override public void onBindViewHolder(RssViewHolder holder, int position) {
        RssItem item = rssItems.get(position);
        holder.link = item.link();
        if (!item.thumbnail().isEmpty()){
            //todo show thumbnail
            Picasso.with(context)
                    .load(item.thumbnail())
                    .centerInside()
                    .into(holder.thumbnail);
        }
        holder.title.setText(item.title());

        holder.description.setText(htmlToSimpleText(item.description()));
    }

    private String htmlToSimpleText(String html){
        Spanned spanned = Util.fromHtml(html);
        return spanned.toString().replaceAll("\n", "").trim();
    }

    @Override public int getItemCount() {
        return rssItems.size();
    }

    public void update(List<RssItem> rssItems){
        this.rssItems = rssItems;
        notifyDataSetChanged();
    }

    class RssViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.thumbnail) ImageView thumbnail;
        @BindView(R.id.title) TextView title;
        @BindView(R.id.description) TextView description;

        String link;

        public RssViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(v -> {
                if (getAdapterPosition() != RecyclerView.NO_POSITION){
                    controller.onItemClick(link);
                }
            });

        }
    }
}
