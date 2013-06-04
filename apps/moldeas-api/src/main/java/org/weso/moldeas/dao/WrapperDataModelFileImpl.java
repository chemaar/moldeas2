package org.weso.moldeas.dao;

import java.io.File;
import java.io.IOException;

import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.model.DataModel;
import org.weso.moldeas.dao.impl.CPVDataSource;
import org.weso.moldeas.utils.ApplicationContextLocator;

public class WrapperDataModelFileImpl implements WrapperDataModel {

	DataModel model;
	
	public WrapperDataModelFileImpl(String file) throws IOException{
		this.model = new FileDataModel(new File(file));	
	}
	public WrapperDataModelFileImpl (){

	}
	@Override
	public DataModel getDataModel() {
		return this.model;
	}

}
