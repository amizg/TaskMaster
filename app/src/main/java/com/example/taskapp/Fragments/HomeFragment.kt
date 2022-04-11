package com.example.taskapp.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskapp.MainActivity
import com.example.taskapp.R
import com.example.taskapp.databinding.ActivityMainBinding.inflate


class HomeFragment : Fragment(), RecyclerAdapter.OnItemClickListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dagBtn: View = view.findViewById(R.id.dagBtn)

        dagBtn.setOnClickListener {
            dayAtAGlace()
        }
    }

    private fun dayAtAGlace(){

        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.alert_box_dag, null)

        val alertDialog = MainActivity.alertBuilder

        alertDialog.setView(dialogLayout)
        alertDialog.setTitle("")

        val recyclerView: RecyclerView = dialogLayout.findViewById(R.id.recycler)
        val adapter = RecyclerAdapter(MainActivity.dm.dagTasks(), this)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        alertDialog.setNeutralButton("") { dialog, _ ->
            dialog.dismiss()
        }
        alertDialog.setNegativeButton("") { dialog, _ ->
            dialog.dismiss()
        }
        alertDialog.setPositiveButton("Close"){dialog, _ ->
            dialog.dismiss()
        }
        alertDialog.show()
    }

    override fun onItemClick(position: Int) {
        TODO("Not yet implemented")
    }

}