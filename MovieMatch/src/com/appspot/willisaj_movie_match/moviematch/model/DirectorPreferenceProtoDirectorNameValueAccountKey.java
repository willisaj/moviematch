/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
/*
 * This code was generated by https://code.google.com/p/google-apis-client-generator/
 * (build: 2015-01-14 17:53:03 UTC)
 * on 2015-02-18 at 23:06:41 UTC 
 * Modify at your own risk.
 */

package com.appspot.willisaj_movie_match.moviematch.model;

/**
 * Model definition for DirectorPreferenceProtoDirectorNameValueAccountKey.
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the moviematch. For a detailed explanation see:
 * <a href="http://code.google.com/p/google-http-java-client/wiki/JSON">http://code.google.com/p/google-http-java-client/wiki/JSON</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class DirectorPreferenceProtoDirectorNameValueAccountKey extends com.google.api.client.json.GenericJson {

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key("account_key")
  private java.lang.String accountKey;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key("director_name")
  private java.lang.String directorName;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.Double value;

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getAccountKey() {
    return accountKey;
  }

  /**
   * @param accountKey accountKey or {@code null} for none
   */
  public DirectorPreferenceProtoDirectorNameValueAccountKey setAccountKey(java.lang.String accountKey) {
    this.accountKey = accountKey;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getDirectorName() {
    return directorName;
  }

  /**
   * @param directorName directorName or {@code null} for none
   */
  public DirectorPreferenceProtoDirectorNameValueAccountKey setDirectorName(java.lang.String directorName) {
    this.directorName = directorName;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Double getValue() {
    return value;
  }

  /**
   * @param value value or {@code null} for none
   */
  public DirectorPreferenceProtoDirectorNameValueAccountKey setValue(java.lang.Double value) {
    this.value = value;
    return this;
  }

  @Override
  public DirectorPreferenceProtoDirectorNameValueAccountKey set(String fieldName, Object value) {
    return (DirectorPreferenceProtoDirectorNameValueAccountKey) super.set(fieldName, value);
  }

  @Override
  public DirectorPreferenceProtoDirectorNameValueAccountKey clone() {
    return (DirectorPreferenceProtoDirectorNameValueAccountKey) super.clone();
  }

}
