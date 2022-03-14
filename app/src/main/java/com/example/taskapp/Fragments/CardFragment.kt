package com.example.taskapp.Fragments
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.taskapp.databinding.FragmentCardBinding

class CardFragment(nm: String) : Fragment() {

    private var name: String = "Card"
    private var _binding: FragmentCardBinding? = null
    private val binding get() = _binding!!

    init{
        name = nm
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCardBinding.inflate(inflater, container, false)
        val view = binding.root
        // Inflate the layout for this fragment
        binding.cardName.text = name
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}