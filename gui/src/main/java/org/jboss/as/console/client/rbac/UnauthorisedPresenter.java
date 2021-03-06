package org.jboss.as.console.client.rbac;

import com.google.gwt.user.client.History;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PopupView;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import org.jboss.ballroom.client.layout.LHSHighlightEvent;
import org.jboss.ballroom.client.rbac.AuthorisationDecision;

import javax.inject.Inject;

/**
 * @author Heiko Braun
 * @date 7/24/13
 */
public class UnauthorisedPresenter extends PresenterWidget<UnauthorisedPresenter.MyView>
    implements AuthDecisionEvent.AuthDecisionHandler {


    /**
     * {@link UnauthorisedPresenter}'s view.
     */
    public interface MyView extends PopupView {
        void setPresenter(UnauthorisedPresenter unauthorisedPresenter);

        void setLastDecision(AuthorisationDecision decision);
    }

    @Inject
    public UnauthorisedPresenter(EventBus eventBus, MyView view) {
        super(eventBus, view);
        view.setPresenter(this);
        eventBus.addHandler(AuthDecisionEvent.TYPE, this);
    }

    public void onConfirmation() {
        History.back();
    }

    @Override
    public void onAuthDescision(AuthDecisionEvent event) {
        getView().setLastDecision(event.getDecision());
    }
}
