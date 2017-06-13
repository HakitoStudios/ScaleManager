package havrysh.scalemanager.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import havrysh.scalemanager.R;
import havrysh.scalemanager.domain.Piece;
import havrysh.scalemanager.domain.PieceFacade;
import havrysh.scalemanager.ui.adapter.PieceAdapter;
import havrysh.scalemanager.utils.FormatUtils;

public class PiecesFragment extends Fragment {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.overall_count_text)
    TextView countText;

    @BindView(R.id.overall_price_text)
    TextView priceText;

    @BindView(R.id.overall_weight_text)
    TextView weightText;

    @BindView(R.id.empty_text)
    TextView emptyText;

    private PieceAdapter adapter;
    private PieceFacade facade;

    private Long dateFrom, dateTo;


    public static PiecesFragment newInstance() {

        Bundle args = new Bundle();

        PiecesFragment fragment = new PiecesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pieces, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        facade = new PieceFacade();
        adapter = new PieceAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }


            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                new AlertDialog.Builder(getContext())
                        .setMessage("Удалить?")
                        .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                adapter.getPieces()
                                        .remove(viewHolder.getAdapterPosition())
                                        .delete();
                                adapter.swapData(adapter.getPieces());
                                invalidateItems(dateFrom, dateTo);
                                recalculateStats();
                            }
                        })
                        .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                adapter.notifyItemChanged(viewHolder.getAdapterPosition());
                            }
                        })
                        .setCancelable(false)
                        .create()
                        .show();

            }
        });

        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void recalculateStats() {
        float weight = 0;
        float price = 0;

        for (Piece p : adapter.getPieces()) {
            weight += p.getWeight();
            price += p.getWeight() * p.getPrice() / 1000;
        }

        weightText.setText(String.format("%.3f кг", weight / 1000));
        priceText.setText(FormatUtils.formatMoney(price));
        countText.setText(adapter.getItemCount() + " шт.");

        emptyText.setVisibility(adapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }

    private void displayPieces(List<Piece> pieces) {
        adapter.swapData(pieces);
        recalculateStats();
    }

    public void invalidateItems(Long dateFrom, Long dateTo) {
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        displayPieces(facade.getAll(dateFrom, dateTo));
    }
}
