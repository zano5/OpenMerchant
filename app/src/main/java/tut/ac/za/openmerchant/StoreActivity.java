package tut.ac.za.openmerchant;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.ArrayList;
import java.util.List;

import tut.ac.za.openmerchant.ui.FragmentArtWork;
import tut.ac.za.openmerchant.ui.FragmentClothing;
import tut.ac.za.openmerchant.ui.FragmentConstruction;
import tut.ac.za.openmerchant.ui.FragmentFoodAndDrink;
import tut.ac.za.openmerchant.ui.FragmentFurniture;

public class StoreActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Fresco.initialize(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        viewPager = (ViewPager) findViewById(R.id.viewpager);

        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }



    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());


        FragmentArtWork artWork = new FragmentArtWork();

        FragmentClothing clothing = new FragmentClothing();

        FragmentConstruction construction = new FragmentConstruction();

        FragmentFoodAndDrink foodAndDrink = new FragmentFoodAndDrink();

        FragmentFurniture furniture = new FragmentFurniture();



        adapter.addFragment(artWork, "Art Work");
        adapter.addFragment(clothing, "Clothing");
        adapter.addFragment(construction, "Construction");
        adapter.addFragment(foodAndDrink, "Food And Drink");
        adapter.addFragment(furniture,"Furniture");

        viewPager.setAdapter(adapter);
    }



    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


}
