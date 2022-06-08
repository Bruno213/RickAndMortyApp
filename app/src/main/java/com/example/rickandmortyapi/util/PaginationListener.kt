package com.example.rickandmortyapi.util


import androidx.core.view.isVisible
import androidx.core.view.size
import androidx.recyclerview.widget.RecyclerView


abstract class PaginationListener: RecyclerView.OnScrollListener() {
    private var canScrollV = false

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

       // val recyclerViewHeight = recyclerView.height
       // val childHeight = recyclerView.getChildAt(0).height

        canScrollV = recyclerView.canScrollVertically(1)

        if(!canScrollV && !isLastPage())
        loadNextPage()
    }

    abstract fun loadNextPage()

    abstract fun isLastPage(): Boolean
    //abstract fun isLoading(): Boolean
}