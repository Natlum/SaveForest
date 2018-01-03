package es.jjsr.saveforest.historyAdvicePackage;


import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.jjsr.saveforest.R;
import es.jjsr.saveforest.adapter.AdapterForRecyclerView;
import es.jjsr.saveforest.contentProviderPackage.AdviceProvider;
import es.jjsr.saveforest.contentProviderPackage.Contract;
import es.jjsr.saveforest.dto.Advice;
import es.jjsr.saveforest.resource.LoadAndSaveImage;
import es.jjsr.saveforest.resource.constants.GConstants;

/**
 * Contiene lo necesario para mostrar el listado de avisos.
 * Si se hace clic sobre algún aviso, se abrirá una actividad que muestra el contenido.
 * Si se hace un longclic sobre algún aviso, se mostrará un menú contextual para editar o eliminar el aviso.
 * A simple {@link Fragment} subclass.
 */
public class HistoryAdviceListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{


    private RecyclerView mRecyclerView;
    private AdapterForRecyclerView mAdapter;
    private RecyclerView.LayoutManager mlayoutManager;
    private List<Advice> advices;

    private LoaderManager.LoaderCallbacks<Cursor> mCallbacks;

    private ActionMode mActionMode;
    private int idAdvice;
    private int cardSelected;

    private TextView textAdvicesIsEmpty;


    public HistoryAdviceListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_history_advice_list, container, false);

        textAdvicesIsEmpty = (TextView) v.findViewById(R.id.textAdvicesEmpty);

        mRecyclerView = v.findViewById(R.id.recycler_view_items);
        mRecyclerView.setHasFixedSize(true);

        mlayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mlayoutManager);

        return v;
    }

    private void initializeAdapter(){
        if (advices.isEmpty()){
            mRecyclerView.setVisibility(View.GONE);
            textAdvicesIsEmpty.setVisibility(View.VISIBLE);
        }else {
            textAdvicesIsEmpty.setVisibility(View.GONE);
            mAdapter = new AdapterForRecyclerView(advices, getActivity());
            mAdapter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cardSelected = mRecyclerView.getChildAdapterPosition(v);
                    Intent intent = new Intent(getActivity(), ShowAdviceActivity.class);
                    intent.putExtra("adviceObject", advices.get(cardSelected));
                    startActivity(intent);
                }
            });
            if (GConstants.VERSION_ADMINISTRATOR){
                mAdapter.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        if (mActionMode != null) {
                            return false;
                        }
                        mActionMode = getActivity().startActionMode(mActionModeCallback);
                        view.setSelected(true);
                        idAdvice = advices.get(mRecyclerView.getChildAdapterPosition(view)).getId();
                        cardSelected = mRecyclerView.getChildAdapterPosition(view);
                        return true;
                    }
                });
            }
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mCallbacks = this;
        getLoaderManager().initLoader(0, null, mCallbacks);
    }

    ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            MenuInflater inflater = actionMode.getMenuInflater();
            inflater.inflate(R.menu.menu_context, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            switch (menuItem.getItemId()){
                case R.id.menu_delete:
                    Advice advice = advices.get(cardSelected);
                    LoadAndSaveImage.deleteImage(getContext(), advice.getNameImage());
                    AdviceProvider.deleteRecordWithBinnacle(getActivity().getContentResolver(), idAdvice);
                    break;
                case R.id.menu_edit:
                    Intent intent = new Intent(getActivity(), UpdateAdviceActivity.class);
                    intent.putExtra("adviceObject", advices.get(cardSelected));
                    startActivity(intent);
                    break;
            }
            mActionMode.finish();
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            mActionMode = null;
        }
    };

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String columns[] = new String[] {Contract.Advice.ID_ADVICE,
                Contract.Advice.DESCRIPTION,
                Contract.Advice.DATE,
                Contract.Advice.LATITUDE,
                Contract.Advice.LONGITUDE,
                Contract.Advice.ID_COUNTRY,
                Contract.Advice.NAME_IMAGE};

        Uri baseUri = Contract.Advice.CONTENT_URI_ADVICE;

        String selection = null;
        String sortOrder = Contract.Advice.ID_ADVICE + " DESC";

        return new CursorLoader(getActivity(), baseUri, columns, selection, null, sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        Uri laUriBase = Contract.Advice.CONTENT_URI_ADVICE;
        data.setNotificationUri(getActivity().getContentResolver(), laUriBase);

        if (data != null){
            advices = new ArrayList<>();

            data.moveToFirst();

            SimpleDateFormat originalFormat = new SimpleDateFormat("yyyyMMdd");
            try {
                do{
                    Advice advice = new Advice();
                    advice.setId(Integer.parseInt(data.getString(data.getColumnIndex(Contract.Advice.ID_ADVICE))));
                    advice.setDescription(data.getString(data.getColumnIndex(Contract.Advice.DESCRIPTION)));
                    try {
                        Date date = originalFormat.parse(data.getString(data.getColumnIndex(Contract.Advice.DATE)));
                        advice.setDate(date);
                    } catch (ParseException e) {
                        Date date = new Date(1984, 10, 03);
                        advice.setDate(date);
                    }
                    advice.setLatitude(data.getDouble(data.getColumnIndex(Contract.Advice.LATITUDE)));
                    advice.setLongitude(data.getDouble(data.getColumnIndex(Contract.Advice.LONGITUDE)));
                    advice.setIdCountry(data.getInt(data.getColumnIndex(Contract.Advice.ID_COUNTRY)));
                    advice.setNameImage(data.getString(data.getColumnIndex(Contract.Advice.NAME_IMAGE)));
                    advices.add(advice);
                }while (data.moveToNext());
            }catch (Exception e){
                Log.e("Fail Advices", "Error al cargar los avisos");
            }finally {
                initializeAdapter();
            }

            //Toast.makeText(getActivity(), "Datos cargados de la Base de datos", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getActivity(), getString(R.string.fail_load_countries), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
