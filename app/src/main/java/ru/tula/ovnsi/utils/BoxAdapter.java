package ru.tula.ovnsi.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ru.tula.ovnsi.R;
import ru.tula.ovnsi.ui.stringAddresList;

/**
 * Created by smock210 on 08.11.2016.
 */

public class BoxAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<stringAddresList> objects;

    public BoxAdapter(Context context, ArrayList<stringAddresList> bp) {
        ctx = context;
        objects = bp;
        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // кол-во элементов
    @Override
    public int getCount() {
        return objects.size();
    }

    // элемент по позиции
    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    // id по позиции
    @Override
    public long getItemId(int position) {
        return position;
    }

    // пункт списка
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // используем созданные, но не используемые view
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.mainlayoutbp, parent, false);
        }


        stringAddresList p = getProduct(position);

        // заполняем View в пункте списка данными из товаров: наименование, цена
        // и картинка

        ((TextView) view.findViewById(R.id.idDescr)).setText(p.idBp);
        ((TextView) view.findViewById(R.id.gkh)).setText(p.orgBp);
        ((TextView) view.findViewById(R.id.address)).setText(p.addres);

        return view;
    }


    // товар по позиции
    stringAddresList getProduct(int position) {
        return ((stringAddresList) getItem(position));
    }

    // содержимое корзины
}




