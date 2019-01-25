/*
 * Ranked Choice Voting Universal Tabulator
 * Copyright (c) 2018 Jonathan Moldover, Louis Eisenberg, and Hylton Edingfield
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See
 * the GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License along with this
 * program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Purpose:
 * Wrapper for Jackson JSON parser to parse JSON files into Java objects.
 */

package com.rcv;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;

class JsonParser {

  // function: parseObjectFromFile
  // purpose: parse input json file into an object of the specified type
  // param: jsonFilePath path to json file to be parsed into java
  // param: valueType class of the object to be created from parsed json
  // file access: read
  // returns: instance of the object parsed from json or null if there was a problem
  static <T> T parseObjectFromFile(String jsonFilePath, Class<T> valueType) {
    T createdObject;
    try {
      // fileReader will read the json file from disk
      FileReader fileReader = new FileReader(jsonFilePath);
      // objectMapper will map json values into the new java object
      ObjectMapper objectMapper = new ObjectMapper();
      // object is the newly created object populated with json values
      createdObject = objectMapper.readValue(fileReader, valueType);
    } catch (JsonParseException | JsonMappingException exception) {
      Logger.log(
          Level.SEVERE, "Error parsing JSON file: %s\n%s", jsonFilePath, exception.toString());
      Logger.log(
          Level.SEVERE, "Check your file formatting and values to make sure they are correct.");
      createdObject = null;
    } catch (IOException exception) {
      Logger.log(
          Level.SEVERE, "Error opening file: %s\n%s", jsonFilePath, exception.toString());
      Logger.log(
          Level.SEVERE, "Check your file path and permissions and make sure they are correct.");
      createdObject = null;
    }
    return createdObject;
  }


  static void parseObjectToFile(File jsonFile, Object config) {
    try {
      new ObjectMapper().writer().withDefaultPrettyPrinter().writeValue(jsonFile, config);
      Logger.log(
          Level.INFO, "Saved object to: %s", jsonFile.getAbsolutePath());
    } catch (IOException exception) {
      Logger.log(
          Level.SEVERE,
          "Error saving file: %s\n%s",
          jsonFile.getAbsolutePath(),
          exception.toString());
    }
  }
}
