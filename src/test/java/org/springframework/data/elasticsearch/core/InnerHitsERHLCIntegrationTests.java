/*
 * Copyright 2020-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.elasticsearch.core;

import static org.elasticsearch.index.query.QueryBuilders.*;

import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.InnerHitBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.elasticsearch.client.erhlc.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.junit.jupiter.ElasticsearchRestTemplateConfiguration;
import org.springframework.data.elasticsearch.utils.IndexNameProvider;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author Peter-Josef Meisch
 */
@ContextConfiguration(classes = { InnerHitsERHLCIntegrationTests.Config.class })
public class InnerHitsERHLCIntegrationTests extends InnerHitsIntegrationTests {

	@Configuration
	@Import({ ElasticsearchRestTemplateConfiguration.class })
	static class Config {
		@Bean
		IndexNameProvider indexNameProvider() {
			return new IndexNameProvider("innerhits-es7");
		}
	}

	@Override
	protected Query buildQueryForInnerHits(String innerHitName, String nestedQueryPath, String matchField,
			String matchValue) {
		NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

		NestedQueryBuilder nestedQueryBuilder = nestedQuery(nestedQueryPath, matchQuery(matchField, matchValue),
				ScoreMode.Avg);
		nestedQueryBuilder.innerHit(new InnerHitBuilder(innerHitName));
		queryBuilder.withQuery(nestedQueryBuilder);

		return queryBuilder.build();
	}

}