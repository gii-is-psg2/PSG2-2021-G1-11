/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic.service;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Cause;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
class CauseServiceTests {

	@Autowired
	protected CauseService causeService;

	@Test
	void shouldFindCauseById() {
		final Cause cause = new Cause();
		cause.setDescription("Queremos salvar a los pingüinos");
		cause.setName("Antártida");
		cause.setIsClosed(false);
		cause.setOrganization("Greenpeace");
		cause.setTarget(2000.);
		this.causeService.saveCause(cause);
		
		final Cause cause2 = this.causeService.findCauseById(cause.getId());
		Assertions.assertEquals("Antártida",cause2.getName());
		Assertions.assertEquals(2000.,cause2.getTarget());
		Assertions.assertEquals("Queremos salvar a los pingüinos",cause2.getDescription());
		Assertions.assertEquals(false,cause2.getIsClosed());
		Assertions.assertEquals("Greenpeace",cause2.getOrganization());	
	}

	@Test
	void shouldntFindCauseById() {
		final Cause cause3 = this.causeService.findCauseById(4);
		Assert.assertNull(cause3);
	}
	
	@Test
	void shouldGetActualAmountById() {
		final Cause cause = new Cause();
		cause.setDescription("Queremos salvar a los pingüinos");
		cause.setName("Antártida");
		cause.setIsClosed(false);
		cause.setOrganization("Greenpeace");
		cause.setTarget(2000.);
		this.causeService.saveCause(cause);
		
		final Double amount = this.causeService.actualAmountById(cause.getId());
		Assertions.assertEquals(null,amount);
	}
	
	

}
