package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;
import javafx.scene.web.WebHistory.Entry;
import javafx.stage.Stage;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class ABrowserController implements Initializable {

	@FXML
	private ProgressBar pgbarLoad;

	@FXML
	private TabPane tabPane;

	@FXML
	private TextField tfURL;

	@FXML
	private Button btnNext;

	@FXML
	private Button btnBack;

	private WebView currentWebView;
	private WebEngine currentWebEngine;
	private Stage stage;

//	private final Tabs tabs = new Tabs();

	@FXML
	public void processURL(ActionEvent event) {
		loadURL(tfURL.getText().trim());
	}

	@FXML
	public void processZoomIn(ActionEvent event) {
		currentWebView.setZoom(currentWebView.getZoom() + 0.25);
	}

	@FXML
	public void processReload(ActionEvent event) {
		currentWebView.setZoom(1.0);
	}

	@FXML
	public void processZoomOut(ActionEvent event) {
		currentWebView.setZoom(currentWebView.getZoom() - 0.25);
	}

	@FXML
	public void processNext(ActionEvent event) {
		int index = tabPane.getSelectionModel().getSelectedIndex();
		tabPane.getSelectionModel().select(index + 1);
		index = tabPane.getSelectionModel().getSelectedIndex();
		if (index == tabPane.getTabs().size()-1) {
			btnNext.setDisable(true);
		}
	
		btnBack.setDisable(false);
	}

	@FXML
	public void processBack(ActionEvent event) {
		int index = tabPane.getSelectionModel().getSelectedIndex();
		if (index == 1) {
			btnBack.setDisable(true);
		}
		btnNext.setDisable(false);
		tabPane.getSelectionModel().select(index - 1);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		pgbarLoad.setVisible(false);
		btnBack.setDisable(true);
		btnNext.setDisable(true);
		tabPane.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				if (Tabs.getTabsList() != null) {
					if (tabPane.getSelectionModel().getSelectedItem() != null) {
						for (Tabs tabs : Tabs.getTabsList()) {
							if (tabPane.getSelectionModel().getSelectedItem() == tabs.getTab()) {
								currentWebView = tabs.getWebView();
								currentWebEngine = currentWebView.getEngine();
								Entry currentEntry = currentWebView.getEngine().getHistory().getEntries().get(0);
								tfURL.setText(currentEntry.getUrl().substring(12).replaceAll("/", ""));
								if (!currentEntry.getTitle().equals(""))
									stage.setTitle(currentEntry.getTitle());
								else
									stage.setTitle(
											currentEntry.getUrl().substring(12).replaceAll(".com/", "").toUpperCase());
							}
						}
						if (tabPane.getSelectionModel().getSelectedIndex() == 0)
							btnBack.setDisable(true);
						else
							btnBack.setDisable(false);

						if (tabPane.getSelectionModel().getSelectedIndex() == tabPane.getTabs().size() - 1)
							btnNext.setDisable(true);
						else
							btnNext.setDisable(false);
					}
				}
			}
		});
	}

	void loadURL(String URL) {
		tfURL.setDisable(true);
		WebView newView = new WebView();
		currentWebView = newView;
		currentWebEngine = currentWebView.getEngine();
		currentWebEngine.load("https://www." + URL);
		Tab newTab = new Tab();
		newTab.setClosable(true);
		newTab.setContent(newView);

		tabPane.getTabs().add(newTab);
		Tabs.getTabsList().add(new Tabs(newTab, newView));
		Worker<Void> worker = currentWebEngine.getLoadWorker();
		pgbarLoad.progressProperty().bind(worker.progressProperty());
		pgbarLoad.setVisible(true);
		worker.stateProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
				if (newValue == Worker.State.SUCCEEDED) {
					tfURL.setDisable(false);
					pgbarLoad.setVisible(false);
					tabPane.getSelectionModel().selectLast();
					if (tabPane.getTabs().size() > 1)
						btnBack.setDisable(false);
					btnNext.setDisable(true);
//					WebHistory webHistory = currentWebView.getEngine().getHistory();
//					ObservableList<WebHistory.Entry> entries = webHistory.getEntries();
//					Entry currentEntry = entries.get(webHistory.getCurrentIndex());
					Entry currentEntry = currentWebView.getEngine().getHistory().getEntries().get(0);
					tfURL.setText(currentEntry.getUrl().substring(12).replaceAll("/", ""));
					stage = (Stage) tfURL.getScene().getWindow();
					if (!currentEntry.getTitle().equals("")) {
						newTab.setText(currentEntry.getTitle());
						stage.setTitle(currentEntry.getTitle());
					} else {
						newTab.setText(currentEntry.getUrl().substring(12).replaceAll(".com/", "").toUpperCase());
						stage.setTitle(currentEntry.getUrl().substring(12).replaceAll(".com/", "").toUpperCase());
					}
				}
				if (newValue == Worker.State.FAILED) {
					tfURL.setDisable(false);
					pgbarLoad.setVisible(false);
					System.out.println("Page Loading Failed");
				}
			}
		});
	}

}
