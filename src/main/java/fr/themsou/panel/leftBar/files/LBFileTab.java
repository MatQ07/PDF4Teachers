package fr.themsou.panel.leftBar.files;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import fr.themsou.utils.*;
import fr.themsou.utils.sort.SortManager;
import fr.themsou.utils.sort.Sorter;
import fr.themsou.windows.MainWindow;
import javafx.scene.control.*;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class LBFileTab extends Tab {

	private SortManager sortManager;
	private VBox separator = new VBox();
	private GridPane options = new GridPane();

	public FileListView files = new FileListView();
	public ArrayList<File> originalFiles = new ArrayList<>();

	public LBFileTab(){

		setClosable(false);
		setContent(separator);

		setGraphic(Builders.buildImage(getClass().getResource("/img/pdfdocument.png")+"", 0, 25));
		MainWindow.leftBar.getTabs().add(0, this);

		setup();
	}

	public void setup(){

		files.setOnDragOver((DragEvent e) -> {
			Dragboard db = e.getDragboard();
			if(db.hasFiles()){
				for(File file : db.getFiles()){
					if(isFilePdf(file) || file.isDirectory()){
						e.acceptTransferModes(TransferMode.ANY);
						e.consume();
						return;
					}
				}
			}
			e.consume();
		});
		files.setOnDragDropped((DragEvent e) -> {
			Dragboard db = e.getDragboard();
			if(db.hasFiles()){
				for(File file : db.getFiles()){
					if(isFilePdf(file) || file.isDirectory()){
						File[] files = db.getFiles().toArray(new File[db.getFiles().size()]);
						openFiles(files);
						e.setDropCompleted(true);
						e.consume();
						return;
					}
				}
			}

			e.consume();
		});

		sortManager = new SortManager((sortType, order) -> {
			if(sortType.equals(TR.tr("Nom"))){
				List<File> toSort = files.getItems().stream().collect(Collectors.toList());
				files.getItems().clear();
				files.getItems().addAll(Sorter.sortFilesByName(toSort, order));
			}else if(sortType.equals(TR.tr("Dossier"))){
				List<File> toSort = files.getItems().stream().collect(Collectors.toList());
				files.getItems().clear();
				files.getItems().addAll(Sorter.sortFilesByDir(toSort, order));
			}else if(sortType.equals(TR.tr("Édition"))){
				List<File> toSort = files.getItems().stream().collect(Collectors.toList());
				files.getItems().clear();
				files.getItems().addAll(Sorter.sortFilesByEdit(toSort, order));
			}else if(sortType.equals(TR.tr("Date d'Ajout"))){
				backOpenFilesList(!order);
			}

		}, null, null);
		sortManager.setup(options, TR.tr("Date d'Ajout"), TR.tr("Date d'Ajout"), TR.tr("Édition"), "\n", TR.tr("Nom"), TR.tr("Dossier"));

		separator.getChildren().addAll(options, files);

	}

	private void openFile(File file){
		
		if(!file.isDirectory()){
			if(isFilePdf(file) && !files.getItems().contains(file)){
				files.getItems().add(file);
				originalFiles.add(file);
				sortManager.simulateCall();
			}
		}else{
			for(File VFile : Objects.requireNonNull(file.listFiles())){
				if(isFilePdf(VFile) && !files.getItems().contains(VFile)){
					files.getItems().add(VFile);
					originalFiles.add(VFile);
				}
			}
			sortManager.simulateCall();
		}
	}
	public void openFiles(File[] files){
		for(File file : files){
			openFile(file);
		}
	}
	public void clearFiles(){
		files.getItems().clear();
		updateOpenFilesList();
	}
	public void removeFile(File file){
		files.getItems().remove(file);
		originalFiles.remove(file);
	}
	
	private boolean isFilePdf(File file) {
        String fileName = file.getName();
        String ext = "";
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) 
        	ext = fileName.substring(fileName.lastIndexOf(".") + 1);

		return ext.equalsIgnoreCase("pdf");
	}

	private void updateOpenFilesList(){
		originalFiles.clear();
		originalFiles.addAll(MainWindow.lbFilesTab.files.getItems());
	}
	public void backOpenFilesList(boolean reverse){
		files.getItems().clear();
		ArrayList<File> openedFilesList = (ArrayList<File>) originalFiles.clone();
		if(reverse) Collections.reverse(openedFilesList);
		for(File file : openedFilesList){
			files.getItems().add(file);
		}
	}

}