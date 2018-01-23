package com.ndlp.socialstudy.Skripte;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ndlp.socialstudy.NavigationDrawer_BottomNavigation.MainActivity;
import com.ndlp.socialstudy.R;
import com.ndlp.socialstudy.GeneralFileFolder.VorlesungenObject;
import com.ndlp.socialstudy.Skripte.Vorlesungen.AussenwirtschaftSkripteFragment;
import com.ndlp.socialstudy.Skripte.Vorlesungen.ElektrotechnikSkripteFragment;
import com.ndlp.socialstudy.Skripte.Vorlesungen.InformatikSkripteFragment;
import com.ndlp.socialstudy.Skripte.Vorlesungen.KonstruktionslehreSkripteFragment;
import com.ndlp.socialstudy.Skripte.Vorlesungen.MarketingSkripteFragment;
import com.ndlp.socialstudy.Skripte.Vorlesungen.RechnungswesenSkripteFragment;

import java.util.ArrayList;

/**
 * Class handeling the RecyclerAdapter
 */

public class SkripteVorlesungenRecyclerAdapter extends RecyclerView.Adapter<SkripteVorlesungenRecyclerAdapter.MyViewHolder> {

    private Context context;

    private ArrayList<VorlesungenObject> data;


    //  Konstruktor gets the data from ClassesActivity
    public SkripteVorlesungenRecyclerAdapter(Context context, ArrayList<VorlesungenObject> data){
        this.context = context;
        this.data = data;
    }


    //  get the xml file, give it to the viewholder
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_item, null);

        return new MyViewHolder(itemView);
    }

    //Viewholder wird genutzt um die items vom adapter zu zeigen
    //gets the item position id and handles on click events -> starts activities
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final VorlesungenObject current = data.get(position);
        holder.className.setText(current.getClassTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (holder.getAdapterPosition()){
                    case 0:
                        KonstruktionslehreSkripteFragment konstruktionslehreSkripteFragment = new KonstruktionslehreSkripteFragment();
                        FragmentManager fragmentManager0 = ((MainActivity) context).getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction0 = fragmentManager0.beginTransaction();
                        fragmentTransaction0.replace(R.id.frame_layout, konstruktionslehreSkripteFragment);
                        fragmentTransaction0.addToBackStack(null);
                        fragmentTransaction0.commit();
                        break;

                    case 1:
                        AussenwirtschaftSkripteFragment aussenwirtschaftSkripteFragment = new AussenwirtschaftSkripteFragment();
                        FragmentManager fragmentManager1 = ((MainActivity) context).getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
                        fragmentTransaction1.replace(R.id.frame_layout, aussenwirtschaftSkripteFragment);
                        fragmentTransaction1.addToBackStack(null);
                        fragmentTransaction1.commit();
                        break;

                    case 2:
                        InformatikSkripteFragment informatikSkripteFragment = new InformatikSkripteFragment();
                        FragmentManager fragmentManager2 = ((MainActivity) context).getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction2 = fragmentManager2.beginTransaction();
                        fragmentTransaction2.replace(R.id.frame_layout, informatikSkripteFragment);
                        fragmentTransaction2.addToBackStack(null);
                        fragmentTransaction2.commit();
                        break;

                    case 3:
                        ElektrotechnikSkripteFragment elektrotechnikSkripteFragment = new ElektrotechnikSkripteFragment();
                        FragmentManager fragmentManager3 = ((MainActivity) context).getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction3 = fragmentManager3.beginTransaction();
                        fragmentTransaction3.replace(R.id.frame_layout, elektrotechnikSkripteFragment);
                        fragmentTransaction3.addToBackStack(null);
                        fragmentTransaction3.commit();
                        break;

                    case 4:
                        MarketingSkripteFragment marketingSkripteFragment = new MarketingSkripteFragment();
                        FragmentManager fragmentManager4 = ((MainActivity) context).getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction4 = fragmentManager4.beginTransaction();
                        fragmentTransaction4.replace(R.id.frame_layout, marketingSkripteFragment);
                        fragmentTransaction4.addToBackStack(null);
                        fragmentTransaction4.commit();
                        break;

                    case 5:
                        RechnungswesenSkripteFragment rechnungswesenSkripteFragment = new RechnungswesenSkripteFragment();
                        FragmentManager fragmentManager5 = ((MainActivity) context).getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction5 = fragmentManager5.beginTransaction();
                        fragmentTransaction5.replace(R.id.frame_layout, rechnungswesenSkripteFragment);
                        fragmentTransaction5.addToBackStack(null);
                        fragmentTransaction5.commit();
                        break;

                    default:
                        break;
                }

            }
        });

    }

    //  returns the total count of the items hold by the adapter
    @Override
    public int getItemCount() {
        return data.size();
    }

    //  classifies the view holder and prevents always using findViewById
    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView className;

        public MyViewHolder(View itemView) {
            super(itemView);


            className = (TextView) itemView.findViewById(R.id.tv_classname);


        }

    }
}
