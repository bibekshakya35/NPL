/*
 * Copyright 2008-2011 Grant Ingersoll, Thomas Morton and Drew Farris
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 * -------------------
 * To purchase or learn more about Taming Text, by Grant Ingersoll, Thomas Morton and Drew Farris, visit
 * http://www.manning.com/ingersoll
 */
package com.prasnottar.util;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Map;


/** Encapsulates OpenNLP's NameFinder by providing a mechanism to load
 *  all of the name finder models files found in a single directory into memory
 *  and instantiating an array of NameFinderME objects.
 */
public class NameFinderFactory {
  
  private static final Logger log = LoggerFactory.getLogger(NameFinderFactory.class);

  NameFinderME[] finders;
  String[] modelNames;
 
  public NameFinderFactory() throws IOException {
    this(null);
  }

  public NameFinderFactory(Map<String,String> param) throws IOException {
    String language = OpenNLPUtil.getModelLanguage(param);
    String modelDirectory = OpenNLPUtil.getModelDirectory(param);
    loadNameFinders(language, modelDirectory);
  }
 
  public NameFinderFactory(String language, String modelDirectory) throws IOException {
    loadNameFinders(language, modelDirectory);
  }

  protected File[] findNameFinderModels(String language, String modelDirectory) {
    final String modelPrefix = language + "-ner";

    log.info("Loading name finder models from {} using prefix {} ",
        new Object[] { modelDirectory, modelPrefix } );

    File[] models = new File(modelDirectory).listFiles(new FilenameFilter() {
      public boolean accept(File file, String name) {
        if (name.startsWith(modelPrefix)) {
          return true;
        }
        return false;
      }
    });
    
    if (models == null || models.length < 1) {
      throw new RuntimeException("Configuration Error: No models in " + modelDirectory);
    }
    return models;
  }

  protected void loadNameFinders(String language, String modelDirectory) throws IOException {
    //<start id="maxent.examples.namefinder.setup"/> 
    File modelFile;

    File[] models //<co id="nfe.findmodels"/>
      = findNameFinderModels(language, modelDirectory);
    modelNames = new String[models.length];
    finders = new NameFinderME[models.length];

    for (int fi = 0; fi < models.length; fi++) {
      modelFile = models[fi];
      modelNames[fi] = modelNameFromFile(language, modelFile); //<co id="nfe.modelname"/>
      
      log.info("Loading model {}", modelFile); 
      InputStream modelStream = new FileInputStream(modelFile);
      TokenNameFinderModel model = //<co id="nfe.modelreader"/>
          new TokenNameFinderModel(modelStream);
      finders[fi] = new NameFinderME(model);
      
    }

  }

  /** Extract the model name from the model file, this is used to display
   *  the type of named entity found
   * @param language
   * @param modelFile
   * @return
   */
  protected String modelNameFromFile(String language, File modelFile) {
    String modelName = modelFile.getName();
    return modelName.replace(language + "-ner-", "").replace(".bin", "");
  }

  /** Obtain a reference to the array of NameFinderME's loaded by the engine. 
   * @return
   */
  public NameFinderME[] getNameFinders() {
    return finders;
  }


  public String[] getModelNames() {
    return modelNames;
  }

}
