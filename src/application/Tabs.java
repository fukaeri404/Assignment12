package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Tab;

import javafx.scene.web.WebView;

public final class Tabs {
	private Tab tab;
	private WebView webView;

	static final ObservableList<Tabs> TABS_LIST = FXCollections.observableArrayList();
	public Tabs() {
		
	}
	
	public Tabs(Tab tab, WebView webView) {
		super();
		this.tab = tab;
		this.webView = webView;
	}

	public Tab getTab() {
		return tab;
	}

	public void setTab(Tab tab) {
		this.tab = tab;
	}

	public WebView getWebView() {
		return webView;
	}

	public void setWebView(WebView webView) {
		this.webView = webView;
	}

	public static ObservableList<Tabs> getTabsList() {
		return TABS_LIST;
	}
	
	

}
