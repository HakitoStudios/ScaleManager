package havrysh.scalemanager.ui;

import android.content.DialogInterface;
import android.graphics.Canvas;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import havrysh.scalemanager.R;
import havrysh.scalemanager.domain.PieceFacade;
import havrysh.scalemanager.ui.adapter.PieceAdapter;

public class PiecesFragment extends Fragment {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

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
                            }
                        })
                        .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setCancelable(false)
                        .create()
                        .show();

            }
        });

        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public void invalidateItems(Long dateFrom, Long dateTo) {
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        adapter.swapData(facade.getAll(dateFrom, dateTo));
    }
}
