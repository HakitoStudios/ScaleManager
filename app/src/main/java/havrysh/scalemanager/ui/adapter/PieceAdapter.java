package havrysh.scalemanager.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import havrysh.scalemanager.R;
import havrysh.scalemanager.domain.Piece;
import havrysh.scalemanager.utils.FormatUtils;

public class PieceAdapter extends RecyclerView.Adapter<PieceAdapter.PieceHolder> {


    private static final String WEIGHT_FORMAT = "%d г";

    private final List<Piece> pieces = new ArrayList<>();

    @Override
    public PieceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PieceHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_piece, parent, false));
    }

    @Override
    public void onBindViewHolder(PieceHolder holder, int position) {
        holder.bind(pieces.get(position));
    }

    @Override
    public int getItemCount() {
        return pieces.size();
    }

    public void swapData(List<Piece> newData) {
        pieces.clear();
        pieces.addAll(newData);
        notifyDataSetChanged();
    }

    public Piece getItem(int adapterPosition) {
        return pieces.get(adapterPosition);
    }

    public List<Piece> getPieces() {
        return pieces;
    }

    public class PieceHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.date_text)
        TextView dateText;

        @BindView(R.id.price_text)
        TextView priceView;

        @BindView(R.id.per_kg_price_text)
        TextView perKgPriceView;


        @BindView(R.id.weight_text)
        TextView weightText;


        public PieceHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(Piece piece) {
            dateText.setText(DateUtils.formatDateTime(itemView.getContext(), piece.getDate(), 0));
            priceView.setText(FormatUtils.formatMoney(piece.getPrice() * piece.getWeight() / 1000f));
            perKgPriceView.setText(FormatUtils.formatMoney(piece.getPrice()) + "/кг");
            weightText.setText(String.format(WEIGHT_FORMAT, (int) piece.getWeight()));
        }
    }
}
