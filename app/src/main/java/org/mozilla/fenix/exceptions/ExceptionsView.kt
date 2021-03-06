/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.fenix.exceptions

import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.component_exceptions.view.*
import org.mozilla.fenix.R

/**
 * Interface for the ExceptionsViewInteractor. This interface is implemented by objects that want
 * to respond to user interaction on the ExceptionsView
 */
interface ExceptionsViewInteractor {
    /**
     * Called whenever learn more about tracking protection is tapped
     */
    fun onLearnMore()

    /**
     * Called whenever all exception items are deleted
     */
    fun onDeleteAll()

    /**
     * Called whenever one exception item is deleted
     */
    fun onDeleteOne(item: ExceptionsItem)
}

/**
 * View that contains and configures the Exceptions List
 */
class ExceptionsView(
    private val container: ViewGroup,
    val interactor: ExceptionsInteractor
) : LayoutContainer {

    val view: FrameLayout = LayoutInflater.from(container.context)
        .inflate(R.layout.component_exceptions, container, true)
        .findViewById(R.id.exceptions_wrapper)

    override val containerView: View?
        get() = container

    init {
        view.exceptions_list.apply {
            adapter = ExceptionsAdapter(interactor)
            layoutManager = LinearLayoutManager(container.context)
        }
        val learnMoreText = view.exceptions_learn_more.text.toString()
        val textWithLink = SpannableString(learnMoreText).apply {
            setSpan(UnderlineSpan(), 0, learnMoreText.length, 0)
        }
        with(view.exceptions_learn_more) {
            movementMethod = LinkMovementMethod.getInstance()
            text = textWithLink
            setOnClickListener { interactor.onLearnMore() }
        }
    }

    fun update(state: ExceptionsFragmentState) {
        view.exceptions_empty_view.visibility =
            if (state.items.isEmpty()) View.VISIBLE else View.GONE
        view.exceptions_list.visibility = if (state.items.isEmpty()) View.GONE else View.VISIBLE
        (view.exceptions_list.adapter as ExceptionsAdapter).updateData(state.items)
    }
}
