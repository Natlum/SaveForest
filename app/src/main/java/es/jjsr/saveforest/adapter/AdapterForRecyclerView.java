package es.jjsr.saveforest.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.List;

import es.jjsr.saveforest.R;
import es.jjsr.saveforest.dto.Advice;
import es.jjsr.saveforest.resource.LoadAnsSaveImage;

/**
 * Adaptador para crear las tarjetas de los avisos con la información de la base de datos.
 * Created by José Juan Sosa Rodríguez on 12/10/2017.
 */

public class AdapterForRecyclerView
        extends RecyclerView.Adapter<AdapterForRecyclerView.ViewHolder>
        implements View.OnClickListener, View.OnLongClickListener {

    List<Advice> advices;
    private View.OnClickListener listener;
    private View.OnLongClickListener longListener;
    private Context ctx;

    public AdapterForRecyclerView(List<Advice> advices, Context ctx) {
        this.advices = advices;
        this.ctx = ctx;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_advice, parent, false);
        v.setOnClickListener(this);
        v.setOnLongClickListener(this);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SimpleDateFormat newFormat = new SimpleDateFormat("dd-MM-yyyy");
        String formatedDate = newFormat.format(advices.get(position).getDate());
        String tag = advices.get(position).getId() + " " + formatedDate;
        holder.textView.setText(tag);
        if (advices.get(position).getNameImage() != null){
            try {
                LoadAnsSaveImage.loadImageFromStorage(ctx, advices.get(position).getNameImage(), holder.imageViewCardAdvice);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        return advices.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    public void setOnLongClickListener(View.OnLongClickListener longListener){
        this.longListener = longListener;
    }

    @Override
    public void onClick(View view) {
        if (listener != null)
            listener.onClick(view);
    }

    @Override
    public boolean onLongClick(View view) {
        if (longListener != null){
            longListener.onLongClick(view);
            return true;
        }else {
            return false;
        }
    }


    //////////////////////////////////////////////////////////////////////////////////

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView textView;
        ImageView imageViewCardAdvice;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView)itemView.findViewById(R.id.item_card_view);
            textView = (TextView)itemView.findViewById(R.id.textTitleCardView);
            imageViewCardAdvice = (ImageView)itemView.findViewById(R.id.imageViewCardAdvice);
        }
    }

}
