package com.example.mojaram

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class ShopListAdapter (val context: Context, val shopList: ArrayList<ShopListData>) : BaseAdapter(){
    override fun getCount(): Int {
        return shopList.size
    }

    override fun getItem(position: Int): Any {
        return  shopList[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view : View = LayoutInflater.from(context).inflate(R.layout.shop_item, null)

        val shopName = view.findViewById<TextView>(R.id.shopnameArea)
        val shopAdd = view.findViewById<TextView>(R.id.shopaddArea)
        val shopKeyword = view.findViewById<TextView>(R.id.shopkeywardArea)
        val shopImage = view.findViewById<ImageView>(R.id.shopimageArea)

        val shop = shopList[position]

        shopName.text = shop.shopName
        shopAdd.text = shop.shopAdd
        shopKeyword.text = shop.shopKeyword
        shopImage.setImageDrawable(shop.shopImage)




        return view
    }

}