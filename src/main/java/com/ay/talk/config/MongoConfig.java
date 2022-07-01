package com.ay.talk.config;

import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;





@Configuration
@PropertySource("classpath:application.properties")
public class MongoConfig {	
	@Value("${mongodb.path}")
	String mongodbPath;
	@Value("${mongodb.dbname}")
	String mongodbName;
	
	public @Bean MongoClient mongoClient() {
        ConnectionString connectionString = new ConnectionString(mongodbPath);
        CodecRegistry pojoCodecRegistry = CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build());
        CodecRegistry codecRegistry = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);
        MongoClientSettings clientSettings = MongoClientSettings.builder()
                                                                .applyConnectionString(connectionString)
                                                                .codecRegistry(codecRegistry)
                                                                .build();
        MongoClient mongo=MongoClients.create(clientSettings);
		return mongo;		
	}
	
	public @Bean MongoTemplate mongoTemplate() {
		MongoDatabaseFactory factory=new SimpleMongoClientDatabaseFactory(mongoClient(),mongodbName);
		MappingMongoConverter converter=new MappingMongoConverter(new DefaultDbRefResolver(factory), new MongoMappingContext());
		converter.setTypeMapper(new DefaultMongoTypeMapper(null));
		
		return new MongoTemplate(factory, converter);	
	}
	
	public @Bean MongoDatabase mongoDatabase() { //Insert Update ¿ë
		return mongoClient().getDatabase(mongodbName);
	}
}
