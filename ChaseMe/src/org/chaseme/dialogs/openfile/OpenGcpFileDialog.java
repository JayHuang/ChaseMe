package org.chaseme.dialogs.openfile;

import java.util.List;

import org.chaseme.file.IO.GcpReader;
import org.chaseme.gcp.Gcp;


public abstract class OpenGcpFileDialog extends OpenFileDialog {
	public abstract void onGcpFileLoaded(List<Gcp> gcpList);

	@Override
	protected FileReader createReader() {
		return new GcpReader();
	}

	@Override
	protected void onDataLoaded(FileReader reader) {
		onGcpFileLoaded(((GcpReader) reader).gcpList);
	}
}
