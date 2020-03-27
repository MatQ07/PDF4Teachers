package fr.themsou.panel;

import fr.themsou.document.editions.Edition;
import fr.themsou.document.render.export.ExportWindow;
import fr.themsou.main.AboutWindow;
import fr.themsou.main.Main;
import fr.themsou.panel.LeftBar.LBFilesListView;
import fr.themsou.utils.*;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import java.awt.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.*;
import java.util.List;

@SuppressWarnings("serial")
public class MenuBar extends javafx.scene.control.MenuBar{

	////////// FICHIER //////////

	Menu fichier = new Menu("Fichier");
	NodeMenuItem fichier1Open = createMenuItem("Ouvrir un·des fichiers", "ouvrir", "Ctrl+O",
			"Ajoute un ou plusieurs fichiers dans le panneau des fichiers.");

	NodeMenuItem fichier2OpenDir = createMenuItem("Ouvrir un dossier", "directory", "Ctrl+Shift+O",
			"Ajoute tous les fichiers PDF d'un dossier dans le panneau des fichiers;");

	NodeMenuItem fichier3Clear = createMenuItem("Vider la liste", "vider", "Ctrl+Shift+W",
			"Vide la liste des fichiers", false, true, 0);

	NodeMenuItem fichier4Save = createMenuItem("Sauvegarder l'édition", "sauvegarder", "Ctrl+S",
			"Sauvegarde les éléments d'édition du document ouvert. (Mais ne modifie pas le fichier PDF pré-existant)", true, false, 0);

	NodeMenuItem fichier5Delete = createMenuItem("Supprimer l'édition", "supprimer", "",
			"Supprime les éléments d'édition du document ouvert.", true, false, 0);

	NodeMenuItem fichier6DeleteAll = createMenuItem("Supprimer les éditions des fichiers ouvert", "supprimer", "",
			"Supprime les éditions de tous les fichiers ouverts.");

	NodeMenuItem fichier7Close = createMenuItem("Fermer le document", "fermer", "Ctrl+W",
			"Ferme le document actuellement ouvert.", true, false, 0);

	Menu fichier8SameName = createSubMenu("Éditions des documents du même nom", "memeNom",
			"Intervertis l'édition de ce document avec celle d'un autre document qui porte le même nom. Cette option peut être utilisé lorsqu'un fichier PDF est déplacé. En effet, si un document PDF est déplacé dans un autre dossier, PDFTeacher n'arrivera plus à récupérer son édition, sauf avec cette fonction.", true);

	NodeMenuItem fichier8SameNameNull = createMenuItem("Aucune édition trouvée", "", "",
			"");

	NodeMenuItem fichier9Export = createMenuItem("Exporter (Regénérer le PDF)", "exporter", "Ctrl+E",
			"Créee un nouveau fichier PDF à partir de celui ouvert, avec tous les éléments ajoutés.", true, false, 0);

	NodeMenuItem fichier10ExportAll = createMenuItem("Tout exporter", "exporter", "Ctrl+Shift+E",
			"Créee des nouveau fichiers PDF à partir chacun des fichiers de la liste des fichiers, avec pour chaque fichier, tous les éléments de son édition.", false, true, 0);

	////////// PREFS //////////

	Menu preferences = new Menu("Préférences");

	NodeMenuItem preferences1Zoom = createMenuItem("Zoom lors de l'ouverture d'un document", "zoom", "",
			"Définis le zoom par défaut lors de l'ouverture d'un document.", 30);

	NodeRadioMenuItem preferences2Save = createRadioMenuItem("Sauvegarder automatiquement", "sauvegarder",
			"Sauvegarde l'édition du document automatiquement lors de la fermeture du document ou de l'application.", true);

	NodeRadioMenuItem preferences3Regular = createRadioMenuItem("Sauvegarder régulièrement", "sauvegarder-recharger",
			"Sauvegarde l'édition du document automatiquement toutes les x minutes.", false);

	NodeRadioMenuItem preferences4Restore = createRadioMenuItem("Toujours restaurer la session précédente", "recharger",
			"Réouvre les derniers fichiers ouverts lors de l'ouverture de l'application.", true);

	NodeRadioMenuItem preferences5RemoveWhenAdd = createRadioMenuItem("Supprimer l'élément des éléments précédents\nlorsqu'il est ajouté aux favoris", "favoris",
			"Dans la liste des derniers éléments textuels utilisés, retire automatiquement l'élément lorsqu'il est ajouté aux favoris.", true);

	NodeRadioMenuItem preferences6ShowStart = createRadioMenuItem("N'afficher que le début des éléments textuels", "lines",
			"Dans les liste des éléments textuels, n'affiche que les deux premières lignes de l'élément.", true);

	NodeRadioMenuItem preferences7SmallFont = createRadioMenuItem("Réduire la taille des éléments dans les listes", "cursor",
			"Dans les liste des éléments textuels, affiche les éléments en plus petit.", true);


	////////// OTHER //////////

	Menu apropos = new Menu();
	Menu aide = new Menu("Aide");
	MenuItem aide1Doc = new MenuItem("Charger le document d'aide     ");
	MenuItem aide2Probleme = new MenuItem("Demander de l'aide sur GitHub     ");

	public MenuBar(){
		setup();
	}

	public void setup(){

		////////// FICHIER //////////

		fichier8SameName.disableProperty().bind(Bindings.createBooleanBinding(() -> {return Main.mainScreen.statusProperty().get() != -1;}, Main.mainScreen.statusProperty()));
		fichier8SameName.getItems().add(fichier8SameNameNull);

		fichier.getItems().addAll(fichier1Open, fichier2OpenDir, fichier3Clear, new SeparatorMenuItem(), fichier4Save, fichier5Delete, fichier6DeleteAll, fichier7Close, fichier8SameName, new SeparatorMenuItem(), fichier9Export, fichier10ExportAll);

		////////// PREFS //////////

		preferences2Save.selectedProperty().set(Main.settings.isAutoSave());
		preferences3Regular.selectedProperty().set(Main.settings.getRegularSaving() != -1);
		preferences4Restore.selectedProperty().set(Main.settings.isRestoreLastSession());
		preferences5RemoveWhenAdd.selectedProperty().set(Main.settings.isRemoveElementInPreviousListWhenAddingToFavorites());
		preferences6ShowStart.selectedProperty().set(Main.settings.isShowOnlyStartInTextsList());
		preferences7SmallFont.selectedProperty().set(Main.settings.isSmallFontInTextsList());


		Main.settings.autoSavingProperty().bind(preferences2Save.selectedProperty());
		Main.settings.restoreLastSessionProperty().bind(preferences4Restore.selectedProperty());
		Main.settings.removeElementInPreviousListWhenAddingToFavoritesProperty().bind(preferences5RemoveWhenAdd.selectedProperty());
		Main.settings.showOnlyStartInTextsListProperty().bind(preferences6ShowStart.selectedProperty());
		Main.settings.smallFontInTextsListProperty().bind(preferences7SmallFont.selectedProperty());

		preferences.getItems().addAll(preferences2Save, preferences3Regular, new SeparatorMenuItem(), preferences1Zoom, preferences4Restore, new SeparatorMenuItem(), preferences5RemoveWhenAdd, preferences6ShowStart, preferences7SmallFont);

		////////// OTHER //////////

		aide1Doc.setGraphic(Builders.buildImage(getClass().getResource("/img/MenuBar/info.png")+"", 0, 0));
		aide2Probleme.setGraphic(Builders.buildImage(getClass().getResource("/img/MenuBar/partager.png")+"", 0, 0));
		aide.getItems().addAll(aide1Doc, aide2Probleme);

		/////////////////////////////

		////////// FICHIER //////////

		fichier1Open.setOnAction((ActionEvent actionEvent) -> {

			final FileChooser chooser = new FileChooser();
			chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Fichier PDF", "*.pdf"));
			chooser.setTitle("Selectionner un ou plusieurs fichier");
			chooser.setInitialDirectory((LBFilesListView.lastDirChoosed.exists() ? LBFilesListView.lastDirChoosed : new File(System.getProperty("user.home"))));

			List<File> listFiles = chooser.showOpenMultipleDialog(Main.window);
			if(listFiles != null){
				File[] files = new File[listFiles.size()];
				files = listFiles.toArray(files);
				Main.lbFilesTab.openFiles(files);
				if(files.length == 1){
					Main.mainScreen.openFile(files[0]);
				}
				LBFilesListView.lastDirChoosed = files[0].getParentFile();

			}
		});
		fichier2OpenDir.setOnAction((ActionEvent actionEvent) -> {

			final DirectoryChooser chooser = new DirectoryChooser();
			chooser.setTitle("Selectionner un dossier");
			chooser.setInitialDirectory((LBFilesListView.lastDirChoosed.exists() ? LBFilesListView.lastDirChoosed : new File(System.getProperty("user.home"))));

			File file = chooser.showDialog(Main.window);
			if(file != null) {
				Main.lbFilesTab.openFiles(new File[]{file});
				LBFilesListView.lastDirChoosed = file.getParentFile();
			}
		});
		fichier3Clear.setOnAction((ActionEvent actionEvent) -> {
			Main.lbFilesTab.clearFiles(true);
		});
		fichier4Save.setOnAction((ActionEvent actionEvent) -> {
			if(Main.mainScreen.hasDocument(true)){
				Main.mainScreen.document.edition.save();
			}
		});
		fichier5Delete.setOnAction((ActionEvent e) -> {
			if(Main.mainScreen.hasDocument(true)){
				Main.mainScreen.document.edition.clearEdit(true);
			}
		});
		fichier6DeleteAll.setOnAction((ActionEvent e) -> {
			Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
			new JMetro(dialog.getDialogPane(), Style.LIGHT);
			Builders.secureAlert(dialog);
			dialog.setTitle("Supprimer les éditions");
			dialog.setHeaderText("Êtes vous sûr de vouloir supprimer toutes les éditions des fichiers de la liste ?");


			float yesButSize = FilesUtils.convertOctetToMo(FilesUtils.getSize(new File(Main.dataFolder + "editions")));
			float yesSize = 0L;
			for(File file : Main.lbFilesTab.files.getItems()){
				File editFile = Edition.getEditFile(file);
				yesSize += FilesUtils.getSize(editFile);
			}yesSize = FilesUtils.convertOctetToMo((long) yesSize);

			ButtonType cancel = new ButtonType("Non", ButtonBar.ButtonData.CANCEL_CLOSE);
			ButtonType yes = new ButtonType("Oui (" + yesSize + "Mo)", ButtonBar.ButtonData.OK_DONE);
			ButtonType yesBut = new ButtonType("Supprimer l'ensemble des\néditions enregistrées (" + yesButSize + "Mo)", ButtonBar.ButtonData.OTHER);
			dialog.getButtonTypes().setAll(yesBut, cancel, yes);

			Optional<ButtonType> option = dialog.showAndWait();
			float size = 0L;
			if(option.get() == yes){
				if(Main.mainScreen.hasDocument(false)) Main.mainScreen.document.edition.clearEdit(false);
				for(File file : Main.lbFilesTab.files.getItems()){
					Edition.getEditFile(file).delete();
				}
				size = yesSize;
			}else if(option.get() == yesBut){
				if(Main.mainScreen.hasDocument(false)) Main.mainScreen.document.edition.clearEdit(false);
				for(File file : new File(Main.dataFolder + "editions").listFiles()){
					file.delete();
				}
				size = yesButSize;
			}else return;

			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			new JMetro(alert.getDialogPane(), Style.LIGHT);
			Builders.secureAlert(alert);
			alert.setTitle("Supression terminée");
			alert.setHeaderText("Vos éditions ont bien été supprimés.");
			alert.setContentText("Vous avez supprimé " + size + "Mo");
			alert.show();
		});
		fichier7Close.setOnAction((ActionEvent e) -> {
			if(Main.mainScreen.hasDocument(true)){
				Main.mainScreen.closeFile(true);
			}
		});
		fichier8SameName.setOnShowing((Event event) -> {
			fichier8SameName.getItems().clear();
			int i = 0;
			for(File file : Edition.getEditFilesWithSameName(Main.mainScreen.document.getFile())) {

				MenuItem item = new MenuItem(file.getParentFile().getAbsolutePath().replace(System.getProperty("user.home"), "~") + File.separator);
				fichier8SameName.getItems().add(item);
				item.setOnAction((ActionEvent actionEvent) -> {
					Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
					new JMetro(dialog.getDialogPane(), Style.LIGHT);
					Builders.secureAlert(dialog);
					dialog.setTitle("Charger une autre édition");
					dialog.setHeaderText("Êtes vous sûr de vouloir remplacer l'édition courante par celle-ci ?");

					ButtonType cancel = new ButtonType("Non", ButtonBar.ButtonData.CANCEL_CLOSE);
					ButtonType yes = new ButtonType("Oui", ButtonBar.ButtonData.OK_DONE);
					ButtonType yesAll = new ButtonType("Oui, répéter cette action pour tous les fichiers\nde la liste et du même dossier", ButtonBar.ButtonData.OTHER);
					dialog.getButtonTypes().setAll(cancel, yes, yesAll);

					Optional<ButtonType> option = dialog.showAndWait();
					if(option.get() == yes){
						if(Main.mainScreen.hasDocument(true)){
							Main.mainScreen.document.edition.clearEdit(false);
							Edition.mergeEditFileWithDocFile(file, Main.mainScreen.document.getFile());
							Main.mainScreen.document.loadEdition();
						}
					}else if(option.get() == yesAll){
						if(Main.mainScreen.hasDocument(true)){
							Main.mainScreen.document.edition.clearEdit(false);
							Edition.mergeEditFileWithDocFile(file, Main.mainScreen.document.getFile());
							Main.mainScreen.document.loadEdition();

							for(File otherFileDest : Main.lbFilesTab.files.getItems()){
								if(otherFileDest.getParentFile().getAbsolutePath().equals(Main.mainScreen.document.getFile().getParentFile().getAbsolutePath()) && !otherFileDest.equals(Main.mainScreen.document.getFile())){
									File fromEditFile = Edition.getEditFile(new File(file.getParentFile().getAbsolutePath() + "/" + otherFileDest.getName()));
									if(fromEditFile.exists()){
										Edition.mergeEditFileWithEditFile(fromEditFile, Edition.getEditFile(otherFileDest));
									}else{
										Alert alert = new Alert(Alert.AlertType.ERROR);
										new JMetro(alert.getDialogPane(), Style.LIGHT);
										alert.setTitle("Fichier introuvable");
										alert.setHeaderText("Le fichier correspondant à \"" + otherFileDest.getName() + "\" dans \"" + file.getParentFile().getAbsolutePath().replace(System.getProperty("user.home"), "~") + "\" n'a pas d'édition.");
										ButtonType ok = new ButtonType("Ignorer", ButtonBar.ButtonData.OK_DONE);
										ButtonType cancelAll = new ButtonType("Tout Arreter", ButtonBar.ButtonData.CANCEL_CLOSE);
										alert.getButtonTypes().setAll(ok, cancelAll);
										Builders.secureAlert(alert);
										Optional<ButtonType> option2 = alert.showAndWait();
										if(option2.get() == cancelAll) return;
									}
								}
							}
						}
					}
				});
				i++;
				if(i == 0) fichier8SameName.getItems().add(fichier8SameNameNull);
				Builders.setMenuSize(fichier8SameName);
			}
		});
		fichier9Export.setOnAction((ActionEvent actionEvent) -> {

			Main.mainScreen.document.save();
			new ExportWindow(Collections.singletonList(Main.mainScreen.document.getFile()));

		});
		fichier10ExportAll.setOnAction((ActionEvent actionEvent) -> {

			if(Main.mainScreen.hasDocument(false))
				Main.mainScreen.document.save();

			new ExportWindow(Main.lbFilesTab.files.getItems());

		});

		////////// PREFS //////////

		preferences1Zoom.setOnAction((ActionEvent actionEvent) -> {

			List<Integer> choices = new ArrayList<>(Arrays.asList(50, 70, 80, 90, 100, 110, 120, 140, 160, 180, 200, 230, 250, 280, 300));
			ChoiceDialog<Integer> dialog = new ChoiceDialog<>(Main.settings.getDefaultZoom(), choices);
			new JMetro(dialog.getDialogPane(), Style.LIGHT);
			Builders.secureAlert(dialog);
			dialog.setTitle("Zoom par défaut");
			dialog.setHeaderText("Zoom par défaut lors de l'ouverture d'un document");
			dialog.setContentText("Choisir un pourcentage :");

			Optional<Integer> newZoom = dialog.showAndWait();
			if(!newZoom.isEmpty()){
				Main.settings.setDefaultZoom(newZoom.get());
			}

		});
		preferences3Regular.setOnAction((ActionEvent actionEvent) -> {

			Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);

			HBox pane = new HBox();
			ComboBox<Integer> combo = new ComboBox<>(FXCollections.observableArrayList(1, 5, 10, 15, 20, 30, 45, 60));
			combo.getSelectionModel().select(Main.settings.getRegularSaving() == -1 ? (Integer) 5 : (Integer) Main.settings.getRegularSaving());
			combo.setStyle("-fx-padding-left: 20px;");
			CheckBox activated = new CheckBox("Activer");
			activated.setSelected(Main.settings.getRegularSaving() != -1);
			pane.getChildren().add(0, activated);
			pane.getChildren().add(1, combo);
			HBox.setMargin(activated, new Insets(5, 0, 0, 10));
			HBox.setMargin(combo, new Insets(0, 0, 0, 30));

			combo.disableProperty().bind(activated.selectedProperty().not());

			new JMetro(dialog.getDialogPane(), Style.LIGHT);
			Builders.secureAlert(dialog);

			dialog.setTitle("Sauvegarde régulière");
			dialog.setHeaderText("Définir le nombre de minutes entre deux sauvegardes automatiques.");

			dialog.getDialogPane().setContent(pane);

			ButtonType cancel = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
			ButtonType ok = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
			dialog.getButtonTypes().setAll(cancel, ok);

			Optional<ButtonType> option = dialog.showAndWait();
			if(option.get() == ok){
				if(activated.isSelected()){
					Main.settings.setRegularSaving(combo.getSelectionModel().getSelectedItem());
					preferences3Regular.setSelected(true);
				}else{
					Main.settings.setRegularSaving(-1);
					preferences3Regular.setSelected(false);
				}

			}


		});

		////////// OTHER //////////

		aide1Doc.setOnAction((ActionEvent actionEvent) -> {

			try{
				InputStream docRes = getClass().getResourceAsStream("/Documentation - PDFTeacher.pdf");
				File doc = new File(Main.dataFolder + "" +
						"Documentation - PDFTeacher.pdf");
				if(!doc.exists()) Files.copy(docRes, doc.getAbsoluteFile().toPath());

				Main.mainScreen.openFile(doc);

			}catch(IOException ex){ ex.printStackTrace(); }
		});
		aide2Probleme.setOnAction((ActionEvent actionEvent) -> {
			try{
				Desktop.getDesktop().browse(new URI("https://github.com/themsou/PDFTeacher/issues/new"));
			}catch(IOException | URISyntaxException e){ e.printStackTrace(); }
		});

		Label name = new Label("À propos");
		name.setAlignment(Pos.CENTER_LEFT);
		name.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				new AboutWindow();
			}
		});
		apropos.setGraphic(name);

		// UI Style
		setStyle("-fx-background-color: #2B2B2B;");
		getMenus().addAll(fichier, preferences, apropos, aide);

		for(Menu menu : getMenus()){
			Builders.setMenuSize(menu);
		}

	}

	public Menu createSubMenu(String name, String imgName, String toolTip, boolean disableIfNoDoc){

		Menu menu = new Menu();
		HBox pane = new HBox();

		Label text = new Label(name);
		text.setStyle("-fx-font-size: 13; -fx-padding: 2 0 2 10;"); // top - right - bottom - left


		ImageView icon = Builders.buildImage(getClass().getResource("/img/MenuBar/" + imgName + ".png")+"", 0, 0);

		if(disableIfNoDoc){
			menu.disableProperty().bind(Bindings.createBooleanBinding(() -> Main.mainScreen.statusProperty().get() != -1, Main.mainScreen.statusProperty()));
		}

		Tooltip toolTipUI = new Tooltip(new TextWrapper(toolTip, null, 350).wrap());
		toolTipUI.setShowDuration(Duration.INDEFINITE);
		Tooltip.install(pane, toolTipUI);

		pane.getChildren().addAll(icon, text);
		menu.setGraphic(pane);

		return menu;
	}
	public NodeRadioMenuItem createRadioMenuItem(String text, String imgName, String toolTip, boolean autoUpdate){

		NodeRadioMenuItem menuItem = new NodeRadioMenuItem(new HBox(), text, 350, true, autoUpdate);

		if(imgName != null) menuItem.setImage(Builders.buildImage(getClass().getResource("/img/MenuBar/"+ imgName + ".png")+"", 0, 0));
		if(!toolTip.isBlank()) menuItem.setToolTip(toolTip);

		return menuItem;

	}
	public NodeMenuItem createMenuItem(String text, String imgName, String accelerator, String toolTip, boolean disableIfNoDoc, boolean disableIfNoList, double leftMargin){

		NodeMenuItem menuItem = new NodeMenuItem(new HBox(), text, 350, true);

		if(!imgName.isBlank()) menuItem.setImage(Builders.buildImage(getClass().getResource("/img/MenuBar/"+ imgName + ".png")+"", 0, 0));
		if(!accelerator.isBlank()) menuItem.setAccelerator(accelerator);
		if(!toolTip.isBlank()) menuItem.setToolTip(toolTip);
		if(leftMargin != 0) menuItem.setFalseLeftData(leftMargin);

		if(disableIfNoDoc){
			menuItem.disableProperty().bind(Bindings.createBooleanBinding(() -> Main.mainScreen.statusProperty().get() != -1, Main.mainScreen.statusProperty()));
		}if(disableIfNoList){
			menuItem.disableProperty().bind(Bindings.size(Main.lbFilesTab.files.getItems()).isEqualTo(0));
		}

		return menuItem;

	}
	public NodeMenuItem createMenuItem(String text, String imgName, String accelerator, String toolTip){
		return createMenuItem(text, imgName, accelerator, toolTip, false, false, 0);
	}
	public NodeMenuItem createMenuItem(String text, String imgName, String accelerator, String toolTip, double leftMargin){
		return createMenuItem(text, imgName, accelerator, toolTip, false, false, leftMargin);
	}
}
