package com.example.lab7photo

import android.content.Context
import android.view.*
import android.widget.*

class PhotoAdapter(
    private val ctx: Context,
    private var photos: MutableList<Photo>,
    private var selectionMode: Boolean = false
) : BaseAdapter() {

    override fun getCount() = photos.size
    override fun getItem(pos: Int) = photos[pos]
    override fun getItemId(pos: Int) = pos.toLong()

    override fun getView(pos: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(ctx).inflate(R.layout.item_photo, parent, false)
        val photo = photos[pos]
        view.findViewById<ImageView>(R.id.ivPhoto).setImageResource(photo.resourceId)
        view.findViewById<TextView>(R.id.tvTitle).text = photo.title
        val cb = view.findViewById<CheckBox>(R.id.cbSelect)
        cb.visibility = if (selectionMode) View.VISIBLE else View.GONE
        cb.isChecked  = photo.isSelected
        return view
    }

    fun setSelectionMode(enabled: Boolean) { selectionMode = enabled; notifyDataSetChanged() }
    fun updatePhotos(list: MutableList<Photo>) { photos = list; notifyDataSetChanged() }
    fun getPhotos() = photos
}