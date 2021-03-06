/*******************************************************************************
 *
 * Copyright (C) 2012 by Cognitive Medical Systems, Inc (http://www.cognitivemedciine.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not 
 * use this file except in compliance with the License. You may obtain a copy of 
 * the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed 
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR 
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 ******************************************************************************/
 
 /******************************************************************************
 * Socratic Grid contains components to which third party terms apply. To comply 
 * with these terms, the following notice is provided:
 *
 * TERMS AND CONDITIONS FOR USE, REPRODUCTION, AND DISTRIBUTION
 * Copyright (c) 2008, Nationwide Health Information Network (NHIN) Connect. All 
 * rights reserved.
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 * - Redistributions of source code must retain the above copyright notice, this 
 *   list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright notice, 
 *   this list of conditions and the following disclaimer in the documentation 
 *   and/or other materials provided with the distribution.
 * - Neither the name of the NHIN Connect Project nor the names of its 
 *   contributors may be used to endorse or promote products derived from this 
 *   software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 * END OF TERMS AND CONDITIONS
 *
 ******************************************************************************/
package org.socraticgrid.guvnorassetsscanner.abdera;

import org.socraticgrid.guvnorassetsscanner.ResourceReaderStrategy;
import org.socraticgrid.guvnorassetsscanner.authenticator.GuvnorAuthenticatorProvider;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.abdera.Abdera;
import org.apache.abdera.model.Document;
import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Feed;
import org.apache.abdera.protocol.Response;
import org.apache.abdera.protocol.client.AbderaClient;
import org.apache.abdera.protocol.client.ClientResponse;
import org.apache.abdera.protocol.client.RequestOptions;
import org.apache.commons.io.IOUtils;
import org.drools.mas.util.ResourceDescriptor;

/**
 *
 * @author esteban
 */
public class AbderaResourceReaderStrategy implements ResourceReaderStrategy {

    private GuvnorAuthenticatorProvider authenticatorProvider;
    private String requestURL;

    public AbderaResourceReaderStrategy(String requestURL, GuvnorAuthenticatorProvider authenticatorProvider) {
        this.requestURL = requestURL;
        this.authenticatorProvider = authenticatorProvider;
    }
    
    public List<ResourceDescriptor> getResourceDescriptors(){
        RequestOptions options = this.createRequestOptions();
        
        Abdera abdera = Abdera.getInstance();
        AbderaClient client = new AbderaClient(abdera);
        ClientResponse resp = client.get(requestURL, options);
        
        if (resp.getType() != Response.ResponseType.SUCCESS) {
            throw new RuntimeException(resp.getStatusText());
        }
        
        List<ResourceDescriptor> results = new ArrayList<ResourceDescriptor>();
        
        Document<Feed> document = resp.getDocument();
        for (Entry entry : document.getRoot().getEntries()) {
            results.add(ResourceDescriptorTranslator.toResourceDescriptor(entry));
        }
        
        return results;
    }
    
    public ResourceDescriptor getResourceDescriptor(String url){
        RequestOptions options = this.createRequestOptions();
        
        //TODO: change this to a real encoder
        url = url.replaceAll("\\s", "%20");
        Abdera abdera = Abdera.getInstance();
        AbderaClient client = new AbderaClient(abdera);
        ClientResponse resp = client.get(url, options);
        
        if (resp.getType() != Response.ResponseType.SUCCESS) {
            throw new RuntimeException(resp.getStatusText());
        }
        
        Document<Entry> document = resp.getDocument();
        return ResourceDescriptorTranslator.toResourceDescriptor(document.getRoot());
    }
    
    public String getAssetContent(String sourceURL) throws IOException{
        Abdera abdera = Abdera.getInstance();
        AbderaClient client = new AbderaClient(abdera);
        RequestOptions options = this.createRequestOptions();

        options.setAccept("application/octet-stream");
        
        ClientResponse resp = client.get(sourceURL, options);
        
        if (resp.getType() != Response.ResponseType.SUCCESS) {
            throw new RuntimeException(resp.getStatusText());
        }
        
        return IOUtils.toString(resp.getInputStream());
    }
    
    private RequestOptions createRequestOptions() {
        Abdera abdera = Abdera.getInstance();
        AbderaClient client = new AbderaClient(abdera);
        RequestOptions options = client.getDefaultRequestOptions();

        options.setAccept("application/atom+xml");

        //add authorization parameters
        if (this.authenticatorProvider != null){
            this.authenticatorProvider.addAuthenticationOptions(options);
        }

        return options;
    }
}
