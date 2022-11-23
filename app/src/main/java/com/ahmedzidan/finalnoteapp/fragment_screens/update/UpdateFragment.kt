package com.ahmedzidan.finalnoteapp.fragment_screens.update

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ahmedzidan.finalnoteapp.R
import com.ahmedzidan.finalnoteapp.data.models.TodoData
import com.ahmedzidan.finalnoteapp.data.viewmodel.SharedViewModel
import com.ahmedzidan.finalnoteapp.data.viewmodel.TodoViewModel
import com.ahmedzidan.finalnoteapp.databinding.FragmentUpdateBinding


class UpdateFragment : Fragment() {
private var _binding:FragmentUpdateBinding?=null
    private val noteTodoViewModel: TodoViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()
    private val args by navArgs<UpdateFragmentArgs>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding=FragmentUpdateBinding.inflate(layoutInflater,container,false)

        _binding!!.updateTitleEd.setText(args.currentItem.title)
        _binding!!.updateDescEd.setText(args.currentItem.description)
        _binding!!.updateSpinnerPriorities.setSelection(sharedViewModel.parsePrioritiesToInt(args.currentItem.priority))
        _binding!!.updateSpinnerPriorities.onItemSelectedListener=sharedViewModel.listener
        return _binding!!.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object: MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.update_fragment_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.menu_update_save -> updateNote()
                    R.id.menu_update_delete -> confirmDeleteNote()
                    android.R.id.home -> requireActivity().onBackPressed()
                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }


    private fun confirmDeleteNote() {
      val builder=AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){_,_->
            noteTodoViewModel.deleteData(args.currentItem)
            Toast.makeText(requireContext(),"Successfully Removed: '${args.currentItem.title}'",Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }
        builder.setNegativeButton("No"){_,_->

        }
        builder.setTitle("Delete '${args.currentItem.title}?'")
        builder.setMessage("Are you sure you want to delete this note?")
        builder.create().show()
    }

    private fun updateNote() {
        val noteTitle=_binding!!.updateTitleEd.text.toString()
        val notePriority=_binding!!.updateSpinnerPriorities.selectedItem.toString()
        val noteDescription=_binding!!.updateDescEd.text.toString()
        val dataChecked=sharedViewModel. checkDataFromUser( noteTitle, noteDescription)
        if(dataChecked){
            val noteData=
                TodoData(args.currentItem.id,noteTitle,sharedViewModel. checkPriority(notePriority),noteDescription)
            noteTodoViewModel.updateData(noteData)
            Toast.makeText(requireContext(),"Note Updated Successfully!", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }else{
            Toast.makeText(requireContext(),"Please Fill Out All Fields!", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}