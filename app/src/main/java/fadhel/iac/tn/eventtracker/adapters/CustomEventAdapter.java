package fadhel.iac.tn.eventtracker.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.List;

import fadhel.iac.tn.eventtracker.model.FavouriteEvent;
import fadhel.iac.tn.eventtracker.utils.EventUtils;
import fadhel.iac.tn.eventtracker.utils.MyPopupMenu;
import fadhel.iac.tn.eventtracker.R;
import fadhel.iac.tn.eventtracker.utils.Utils;
import fadhel.iac.tn.eventtracker.activities.DetailActivity;
import fadhel.iac.tn.eventtracker.activities.FavoritesActivity;
import fadhel.iac.tn.eventtracker.activities.ResultActivity;
import fadhel.iac.tn.eventtracker.model.Event;

/**
 * Created by Fadhel on 24/02/2016.
 */
public class CustomEventAdapter extends RecyclerView.Adapter<CustomEventAdapter.ItemViewHolder> {
    // ViewHolder class
    public  class ItemViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener,View.OnLongClickListener {
        CardView cv;
        TextView title;
        TextView date;
        TextView chosenDate;
        TextView heure;
        ImageView thumbnail;

        public ItemViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            title = (TextView) itemView.findViewById(R.id.title);
            date = (TextView) itemView.findViewById(R.id.date);
            heure = (TextView) itemView.findViewById(R.id.heure);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            chosenDate = (TextView) itemView.findViewById(R.id.chosenDate);
            cv.setOnLongClickListener(this);
            cv.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent i = new Intent(context, DetailActivity.class);
            i.putExtra("event", getItems().get(getAdapterPosition()));
            if(context instanceof FavoritesActivity)
                i.putExtra("previous_act","favorisActivity");
            context.startActivity(i);
        }

        @Override
        public boolean onLongClick(View v) {
            if (context instanceof ResultActivity) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                // set title
                alertDialogBuilder.setTitle(context.getString(R.string.ajouter_favoris));

                // set dialog message
                alertDialogBuilder
                        .setMessage(context.getString(R.string.ajouter_aux_favoris))
                        .setCancelable(false)
                        .setPositiveButton(context.getString(R.string.oui), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Event ev = getItems().get(getAdapterPosition());
                                EventUtils.addToFavourite(context, ev);
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton(context.getString(R.string.non), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
                return true;
            } else {
                MyPopupMenu popup = new MyPopupMenu(v.getContext(), v);
                popup.inflate(R.menu.favourite_context_menu);
                popup.setOnMenuItemClickListener(new MyPopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_detail:
                                Intent i = new Intent(context, DetailActivity.class);
                                i.putExtra("event", getItems().get(getAdapterPosition()));
                                if(context instanceof FavoritesActivity)
                                    i.putExtra("previous_act","favorisActivity");
                                context.startActivity(i);
                                break;
                            default:
                                Event ev = getItems().get(getAdapterPosition());
                                EventUtils.removeFromFavourite(context, ev);
                                if(context instanceof FavoritesActivity) {
                                    FavoritesActivity favoritesActivity = (FavoritesActivity) context;
                                    if (ev.getCatégorie().equals("sport")) favoritesActivity.getSportItems().remove(ev);
                                    else favoritesActivity.getCulturalItems().remove(ev);
                                    favoritesActivity.getAllitems().remove(ev);
                                }
                                notifyDataSetChanged();
                                break;
                        }
                        return false;
                    }
                });
                popup.show();
                return true;
            }
        }

    }

    private List<Event> items;
    private Context context;


    public CustomEventAdapter(List<Event> items, Context context) {
        this.items = items;
        this.context = context;
    }

    public void setItems(List<Event> items) {
        this.items = items;
    }
    public List<Event> getItems() {return items;}

    @Override
    public CustomEventAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CustomEventAdapter.ItemViewHolder holder, int i) {
        holder.title.setText(items.get(i).getName());

        String d,d1=null;
        d = Utils.transformDate(items.get(i).getDateStart(), "yyyy-MM-dd", "dd-MM-yyyy");
        if(items.get(i).getDateStop()!=null)
          if(!items.get(i).getDateStop().isEmpty()) {
              d1 = Utils.transformDate(items.get(i).getDateStop(), "yyyy-MM-dd", "dd-MM-yyyy");
             if (items.get(i) instanceof FavouriteEvent) {
                 FavouriteEvent fav = (FavouriteEvent) items.get(i);
                 holder.chosenDate.setVisibility(View.VISIBLE);
                 holder.chosenDate.setText(context.getString(R.string.chosenDate)+":"+Utils.transformDate(fav.getDateReal(),"yyyy-MM-dd","dd-MM-yyyy"));
             }
          }

        if(d1!=null && !d1.equals(d))
        holder.date.setText(context.getString(R.string.du)+" "+d+" "+context.getString(R.string.au)+" "+d1);
        else holder.date.setText(d);
        if(d1!=null && items.get(i).getTimeStop()!=null && !items.get(i).getTimeStop().isEmpty() && !items.get(i).getTimeStart().equals("00:00:00") && !items.get(i).getTimeStop().equals(items.get(i).getTimeStart()) )
        holder.heure.setText(context.getString(R.string.oui)+" "+items.get(i).getTimeStart()+" "+context.getString(R.string.au)+" "+items.get(i).getTimeStop());
        else holder.heure.setText(items.get(i).getTimeStart());
        if(!items.get(i).getImageUrl().isEmpty())
            Picasso.with(context).load(items.get(i).getImageUrl()).into(holder.thumbnail);
        else {
            if(items.get(i).getCatégorie().equals("sport"))
            holder.thumbnail.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.sports));
            else  holder.thumbnail.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.culture));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}