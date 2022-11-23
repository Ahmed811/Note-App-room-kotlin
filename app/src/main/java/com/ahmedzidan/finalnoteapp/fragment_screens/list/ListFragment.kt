package com.ahmedzidan.finalnoteapp.fragment_screens.list

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ahmedzidan.finalnoteapp.R
import com.ahmedzidan.finalnoteapp.data.models.TodoData
import com.ahmedzidan.finalnoteapp.data.viewmodel.SharedViewModel
import com.ahmedzidan.finalnoteapp.data.viewmodel.TodoViewModel
import com.ahmedzidan.finalnoteapp.databinding.FragmentListBinding
import com.google.android.material.snackbar.Snackbar
import jp.wasabeef.recyclerview.animators.*
import com.google.android.gms.ads.AdRequest

class ListFragment : Fragment(), SearchView.OnQueryTextListener {
    private val todoViewModel:TodoViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()
    var sharedPref:SharedPreferences?=null
    private val listAdapter: ListAdapter by lazy { ListAdapter() }
private var _bindind:FragmentListBinding?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
         sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        var style= sharedPref!!.getString("style","grid")



        _bindind=FragmentListBinding.inflate(layoutInflater,container,false)
        _bindind!!.lifecycleOwner=this
        _bindind!!.sharedViewModel=sharedViewModel

        _bindind!!.recyclerView.apply {
            adapter=listAdapter
            // layoutManager=LinearLayoutManager(requireContext())
//             layoutManager=GridLayoutManager(requireContext(),2)

               layoutManager=StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)

          //   layoutManager=StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
            itemAnimator= SlideInUpAnimator().apply{
              addDuration=300
            }
        }

        swipToDelete(_bindind!!.recyclerView)
        todoViewModel.getAllData.observe(viewLifecycleOwner, Observer { data->
            sharedViewModel.checkIfDatabaseIsEmpty(data)
            listAdapter.setData(data)
        })
//        sharedViewModel.emptyDatabase.observe(viewLifecycleOwner, Observer {
//            showEmptyDataViews(it)
//        })
//        _bindind!!.floatingActionAdd.setOnClickListener {
//            findNavController().navigate(R.id.action_listFragment_to_addFragment)
//        }


        return _bindind!!.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adRequest = AdRequest.Builder().build()
        _bindind!!.adView2.loadAd(adRequest)
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.list_fragment_menu, menu)

                val search = menu.findItem(R.id.menu_search)
                val searchView = search.actionView as? SearchView
                searchView?.isSubmitButtonEnabled = true
                searchView?.setOnQueryTextListener(this@ListFragment)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                if(menuItem.itemId==R.id.menu_deleteAll){
                    confirmRemoveAllData()
                }else if (menuItem.itemId==R.id.menuHigh){
                    todoViewModel.sortByHighPriority.observe(viewLifecycleOwner) {
                        listAdapter.setData(it)
                    }
                }else if (menuItem.itemId==R.id.menuLow){
                    todoViewModel.sortByLowPriority.observe(viewLifecycleOwner) {
                        listAdapter.setData(it)
                    }

                }else if (menuItem.itemId==R.id.menu_rate){
                    val appPackage=requireContext().packageName
                    requireContext(). startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackage")))
                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

    }

//    private fun showEmptyDataViews(emptyDatabase:Boolean) {
//        if (emptyDatabase){
//            _bindind!!.noDataImageView.visibility=View.VISIBLE
//            _bindind!!.noDataTextView.visibility=View.VISIBLE
//        }else{
//            _bindind!!.noDataImageView.visibility=View.GONE
//            _bindind!!.noDataTextView.visibility=View.GONE
//        }
//
//    }



    private fun confirmRemoveAllData() {
        val builder= AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){_,_->
           todoViewModel.deleteAllData()
            Toast.makeText(requireContext(),"Successfully Removed All Notes!",
                Toast.LENGTH_SHORT).show()

        }
        builder.setNegativeButton("No"){_,_->

        }
        builder.setTitle("Delete All Notes?!!")
        builder.setMessage("Are you sure you want to delete All notes?")
        builder.create().show()
    }

    private fun swipToDelete(recyclerView: RecyclerView){
        val swipToDeleteCallBack=object :SwipToDelete(){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
               val itemToDelete=listAdapter.dataList[viewHolder.adapterPosition]
                todoViewModel.deleteData(itemToDelete)
                listAdapter.notifyItemChanged(viewHolder.adapterPosition)
                    // Toast.makeText(requireContext(),"Note Removed!",Toast.LENGTH_SHORT).show()
                restoreDeleteData(viewHolder.itemView,itemToDelete,viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper=ItemTouchHelper(swipToDeleteCallBack)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun restoreDeleteData(view: View,deletedItem:TodoData,position:Int){
    val snackbar=Snackbar.make(view,"Deleted '${deletedItem.title}'",Snackbar.LENGTH_LONG)
        snackbar.setAction("Undo"){
            todoViewModel.insertData(deletedItem)
            listAdapter.notifyItemChanged(position)
        }
        snackbar.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _bindind=null
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
       if (query!=null){
           searchThroughDatabase(query)
       }
        return true
    }

    private fun searchThroughDatabase(query: String) {
     var searchQuery=query
        searchQuery="%$searchQuery%"
        todoViewModel.searchDatabase(searchQuery).observe(this, Observer {list->
            list?.let {
                listAdapter.setData(it)
            }

        })
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText!=null){
            searchThroughDatabase(newText)
        }
        return true
    }
}