package com.zattoo.movies.ui.home

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.zattoo.movies.R
import com.zattoo.movies.BR
import com.zattoo.movies.data.home.Movie
import com.zattoo.movies.databinding.FragmentHomeBinding
import com.zattoo.movies.utils.NetworkUtils
import com.zattoo.movies.utils.Resource
import com.zattoo.movies.viewmodel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    @Inject
    lateinit var adapter: HomeAdapter

    @Inject
    lateinit var networkUtils: NetworkUtils

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    private val viewModel: MovieViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false).apply {
            setVariable(BR.vm, viewModel)
            lifecycleOwner = viewLifecycleOwner
        }
        handleResults()
        handleNetwork()
        initUiElements()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun handleResults() {
        lifecycleScope.launch {
            viewModel.stateFlow.collect {
                when (it) {
                    is Resource.Success -> handleResults(it.data)
                    is Resource.Error -> handleError(it.message)
                    is Resource.Loading -> Timber.d("Loading")
                }
            }
        }
    }

    private fun initUiElements() {
        adapter = HomeAdapter()
        binding.recyclerView.adapter = adapter
    }

    private fun handleResults(movies: List<Movie>) {
        if (movies.isEmpty()) {
            handleError()
        } else {
            adapter.submitList(movies)
        }
    }

    private fun handleError(error: String) {
        handleError(false, error)
    }

    private fun handleError() {
        handleError(true, "")
    }

    private fun handleError(isEmptyList: Boolean, error: String) {
        if (isEmptyList) {
            val errorMessage = getString(R.string.empty_list)
            showEmptyList(errorMessage)
        } else {
            showError(error)
        }
    }

    private fun showEmptyList(message: String) {
        binding.recyclerView.visibility = View.GONE
        binding.emptyView.visibility = View.VISIBLE
        binding.emptyView.text = message
    }

    private fun showError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    private fun handleNetwork() {
        networkUtils.getNetworkLiveData().observe(viewLifecycleOwner) { isConnected: Boolean ->
            if (!isConnected) {
                binding.textViewNetworkStatus.text = getString(R.string.text_no_connectivity)
                binding.networkStatusLayout.visibility = View.VISIBLE
                binding.networkStatusLayout.setBackgroundColor(
                    ResourcesCompat.getColor(
                        resources,
                        R.color.colorStatusNotConnected, null
                    )
                )
                binding.swipeRefreshLayout.isRefreshing = false
            } else {
                viewModel.refresh()
                binding.textViewNetworkStatus.text = getString(R.string.text_connectivity)
                binding.networkStatusLayout.setBackgroundColor(
                    ResourcesCompat.getColor(
                        resources, R.color.colorStatusConnected, null
                    )
                )
                binding.networkStatusLayout.animate().alpha(1f)
                    .setStartDelay(ANIMATION_DURATION.toLong())
                    .setDuration(ANIMATION_DURATION.toLong())
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            super.onAnimationEnd(animation)
                            binding.networkStatusLayout.visibility = View.GONE
                        }
                    })
            }
        }
    }
}

private const val ANIMATION_DURATION = 1000