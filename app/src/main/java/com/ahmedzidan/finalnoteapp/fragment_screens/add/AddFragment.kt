package com.ahmedzidan.finalnoteapp.fragment_screens.add

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.ahmedzidan.finalnoteapp.R
import com.ahmedzidan.finalnoteapp.data.models.TodoData
import com.ahmedzidan.finalnoteapp.data.viewmodel.SharedViewModel
import com.ahmedzidan.finalnoteapp.data.viewmodel.TodoViewModel
import com.ahmedzidan.finalnoteapp.databinding.FragmentAddBinding


class AddFragment : Fragment() {
    private val noteTodoViewModel:TodoViewModel by viewModels()
    private val sharedViewModel:SharedViewModel by viewModels()
private var _binding:FragmentAddBinding?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        _binding=FragmentAddBinding.inflate(layoutInflater,container,false)

        _binding!!.spinnerPriorities.onItemSelectedListener=sharedViewModel.listener
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.add_fragment_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                if (menuItem.itemId == R.id.menu_add) {
                    insertNote()
                } else if (menuItem.itemId == android.R.id.home) {
                    requireActivity().onBackPressed()
                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }



    private fun insertNote() {
        val noteTitle=_binding!!.titleEd.text.toString()
        val notePriority=_binding!!.spinnerPriorities.selectedItem.toString()
        val noteDescription=_binding!!.descEd.text.toString()
        val dataChecked=sharedViewModel. checkDataFromUser( noteTitle, noteDescription)
        if(dataChecked){
            val noteData=TodoData(0,noteTitle,sharedViewModel. checkPriority(notePriority),noteDescription)
            noteTodoViewModel.insertData(noteData)
            Toast.makeText(requireContext(),"Note Added Successfully!",Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        }else{
            Toast.makeText(requireContext(),"Please Fill Out All Fields!",Toast.LENGTH_SHORT).show()
        }

    }

}