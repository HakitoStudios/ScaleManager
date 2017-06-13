package havrysh.scalemanager.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import havrysh.scalemanager.R;
import havrysh.scalemanager.domain.Piece;
import havrysh.scalemanager.domain.PieceFacade;
import havrysh.scalemanager.utils.FormatUtils;

public class ScaleActivity extends AppCompatActivity {

    private static final String START_TIME_ARG = "START_TIME_ARG";

    @BindView(R.id.weight_edit_text)
    EditText weightEditText;

    @BindView(R.id.price_edit_text)
    EditText pricePerKg;

    @BindView(R.id.piece_price_text)
    TextView piecePriceText;

    private PieceFacade facade;
    private boolean programmaticalWeightChanging;
    private long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            startTime = System.currentTimeMillis();
        }
        setContentView(R.layout.activity_scale);
        ButterKnife.bind(this);

        facade = new PieceFacade();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_contaiener, PiecesFragment.newInstance())
                .commitNow();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(START_TIME_ARG, startTime);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        startTime = savedInstanceState.getLong(START_TIME_ARG);
    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidatePieces();
    }

    private void invalidatePieces() {
        ((PiecesFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_contaiener))
                .invalidateItems(startTime, null);
    }

    @OnTextChanged({R.id.price_edit_text, R.id.weight_edit_text})
    void onPieceChanged() {
        if (programmaticalWeightChanging) {
            return;
        }
        try {
            float money = Float.valueOf(pricePerKg.getText().toString())
                    * Integer.valueOf(weightEditText.getText().toString()) / 1000f;
            piecePriceText.setText(FormatUtils.formatMoney(money));
        } catch (NumberFormatException e) {
            piecePriceText.setText("");
            return;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.history_action) {
            startActivity(new Intent(this, HistoryActivity.class));
            return true;
        }
        return false;
    }

    @OnClick(R.id.ok_button)
    void onOkClick() {
        Piece piece = new Piece();
        try {
            piece.setPrice(Float.valueOf(pricePerKg.getText().toString()));
            piece.setWeight(Integer.valueOf(weightEditText.getText().toString()));
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid number format", Toast.LENGTH_LONG).show();
            return;
        }
        facade.save(piece);

        invalidatePieces();

        programmaticalWeightChanging = true;
        weightEditText.setText("");
        programmaticalWeightChanging = false;
    }
}
