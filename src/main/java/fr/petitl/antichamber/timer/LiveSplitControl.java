package fr.petitl.antichamber.timer;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import fr.petitl.antichamber.triggers.TriggerInfo;

public class LiveSplitControl implements TimerControl {

    protected Socket socket;
    protected OutputStreamWriter osw;

    public LiveSplitControl() throws UnknownHostException, IOException {
	this("localhost", 16834);
    }

    public LiveSplitControl(String hostname, int port) throws UnknownHostException, IOException {
	socket = new Socket(hostname, port);
	osw = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");

    }

    @Override
    public void exit() {
	reset();
    }

    @Override
    public void buildAndInjectRun(String runName, List<TriggerInfo> splits) {
	reset();
	runName = runName.length() == 0 ? "Run" : runName;
	try {
	    Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
	    Element root = doc.createElement("Run");
	    root.setAttribute("version", "1.6.0");
	    doc.appendChild(root);

	    Element gameIcon = doc.createElement("GameIcon");
	    root.appendChild(gameIcon);

	    Element gameName = doc.createElement("GameName");
	    gameName.setTextContent("Antichamber");
	    root.appendChild(gameName);

	    Element categoryName = doc.createElement("CategoryName");
	    categoryName.setTextContent(runName);
	    root.appendChild(categoryName);

	    Element metadata = doc.createElement("Metadata");
	    {
		Element run = doc.createElement("Run");
		run.setAttribute("id", "");
		metadata.appendChild(run);
		Element platform = doc.createElement("Platform");
		platform.setAttribute("usesEmulator", "False");
		metadata.appendChild(platform);
		Element region = doc.createElement("Region");
		metadata.appendChild(region);
		Element variables = doc.createElement("Variables");
		metadata.appendChild(variables);
	    }
	    root.appendChild(metadata);

	    Element attemptCount = doc.createElement("AttemptCount");
	    attemptCount.setTextContent("0");
	    root.appendChild(attemptCount);

	    Element attemptHistory = doc.createElement("AttemptHistory");
	    root.appendChild(attemptHistory);

	    Element segments = doc.createElement("Segments");
	    for (TriggerInfo ti : splits) {
		segments.appendChild(segment(ti.toString(), doc));
	    }
	    segments.appendChild(segment("Complete " + runName, doc));
	    root.appendChild(segments);

	    Element autoSplitterSettings = doc.createElement("AutoSplitterSettings");
	    root.appendChild(autoSplitterSettings);

	    Transformer transformer = TransformerFactory.newInstance().newTransformer();
	    DOMSource source = new DOMSource(doc);
	    
	    JFileChooser fc = new JFileChooser();
	    fc.addChoosableFileFilter(new FileNameExtensionFilter("LiveSplit Splits (.lss)", "lss"));
	    fc.setFileFilter(fc.getChoosableFileFilters()[1]);
	    if(fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
	    	StreamResult result = new StreamResult(fc.getSelectedFile());
		    transformer.transform(source, result);
	    }
	    
	    
	} catch (ParserConfigurationException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (TransformerException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }

    public Element segment(String segName, Document doc) {
	Element segment = doc.createElement("Segment");
	Element name = doc.createElement("Name");
	name.setTextContent(segName);
	segment.appendChild(name);
	Element icon = doc.createElement("Icon");
	segment.appendChild(icon);
	Element splitTimes = doc.createElement("SplitTimes");
	segment.appendChild(splitTimes);
	Element bestSegmentTime = doc.createElement("BestSegmentTime");
	segment.appendChild(bestSegmentTime);
	Element segmentHistory = doc.createElement("SegmentHistory");
	segment.appendChild(segmentHistory);
	return segment;
    }

    @Override
    public void split(long stopTime) {
	send("split");
    }

    @Override
    public void start(long stopTime) {
	reset();
	send("startorsplit");
    }

    @Override
    public void reset() {
	send("reset");
    }

    protected void send(String str) {
	str += "\r\n";
	try {
	    osw.write(str, 0, str.length());
	    osw.flush();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    @Override
    public String toString() {
	return "livesplit";
    }
    
}
