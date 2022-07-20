package com.sample.movies.ui.home

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
import com.sample.movies.R
import com.sample.movies.BR
import com.sample.movies.data.Movie
import com.sample.movies.databinding.FragmentHomeBinding
import com.sample.movies.utils.NetworkUtils
import com.sample.movies.utils.Resource
import com.sample.movies.viewmodel.MovieViewModel
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
        binding.recyclerView.adapter = adapter
    }

    private fun handleResults(movies: List<Movie>) {
        if (movies.isEmpty()) {
            handleError()
        } else {
            with(binding) {
                recyclerView.visibility = View.VISIBLE
                emptyView.visibility = View.GONE
            }
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
        with(binding) {
            recyclerView.visibility = View.GONE
            emptyView.visibility = View.VISIBLE
            emptyView.text = message
        }
    }

    private fun showError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    private fun handleNetwork() {
        networkUtils.getNetworkLiveData().observe(viewLifecycleOwner) { isConnected: Boolean ->
            if (!isConnected) {
                with(binding) {
                    textViewNetworkStatus.text = getString(R.string.text_no_connectivity)
                    networkStatusLayout.visibility = View.VISIBLE
                    networkStatusLayout.setBackgroundColor(
                        ResourcesCompat.getColor(
                            resources,
                            R.color.colorStatusNotConnected, null
                        )
                    )
                }
            } else {
                with(binding) {
                    textViewNetworkStatus.text = getString(R.string.text_connectivity)
                    networkStatusLayout.setBackgroundColor(
                        ResourcesCompat.getColor(
                            resources, R.color.colorStatusConnected, null
                        )
                    )
                    networkStatusLayout.animate().alpha(1f)
                        .setStartDelay(ANIMATION_DURATION.toLong())
                        .setDuration(ANIMATION_DURATION.toLong())
                        .setListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator) {
                                super.onAnimationEnd(animation)
                                _binding?.networkStatusLayout?.visibility = View.GONE
                            }
                        })
                }
            }
        }
    }
}

private const val ANIMATION_DURATION = 1000