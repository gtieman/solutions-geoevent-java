/*
 | Copyright 2014 Esri
 |
 | Licensed under the Apache License, Version 2.0 (the "License");
 | you may not use this file except in compliance with the License.
 | You may obtain a copy of the License at
 |
 |    http://www.apache.org/licenses/LICENSE-2.0
 |
 | Unless required by applicable law or agreed to in writing, software
 | distributed under the License is distributed on an "AS IS" BASIS,
 | WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 | See the License for the specific language governing permissions and
 | limitations under the License.
 */
package com.esri.geoevent.solutions.adapter.regexText;

import com.esri.ges.adapter.Adapter;
import com.esri.ges.core.component.ComponentException;
import com.esri.ges.adapter.text.TextInboundAdapterService;
import com.esri.ges.adapter.util.XmlAdapterDefinition;

public class RegexTextInboundAdapterService extends TextInboundAdapterService
{
	public RegexTextInboundAdapterService()
	{
	  super();
	  definition = new XmlAdapterDefinition(getResourceAsStream("inboundadapter-definition.xml"));
	}
	
	@Override
	public Adapter createAdapter() throws ComponentException
	{
	  return new RegexTextInboundAdapter(definition);
	}
}