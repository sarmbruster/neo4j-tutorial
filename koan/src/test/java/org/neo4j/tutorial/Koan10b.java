package org.neo4j.tutorial;

import java.util.Collection;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.schema.IndexDefinition;
import org.neo4j.graphdb.schema.Schema;
import org.neo4j.helpers.collection.IteratorUtil;

import static org.junit.Assert.assertEquals;

/**
 * In this Koan we explore the new features on Neo4j: Labels, Schema Indexes and Constraints
 */
public class Koan10b
{

    private static EmbeddedDoctorWhoUniverse universe;
    private static ExecutionEngine executionEngine;
    private Transaction tx;

    @BeforeClass
    public static void createDatabase() throws Exception
    {
        universe = new EmbeddedDoctorWhoUniverse( new DoctorWhoUniverseGenerator() );
        executionEngine = new ExecutionEngine( universe.getDatabase() );
    }

    @AfterClass
    public static void closeTheDatabase()
    {
        universe.stop();
    }

    @Before
    public void openTransaction()
    {
        tx = universe.getDatabase().beginTx();
    }

    @After
    public void closeTransaction()
    {
        tx.close();
    }

    @Test
    public void shouldCreateSchemaIndexOnCharactersInJava()
    {
        Schema schema = universe.getDatabase().schema();

        assertEquals( 0, IteratorUtil.count( schema.getIndexes( DoctorWhoLabels.Character ) ) );

        // YOUR CODE GOES HERE
        // SNIPPET_START

        schema.indexFor( DoctorWhoLabels.Character ).on( "character" ).create();

        // SNIPPET_END

        Collection<IndexDefinition> indexes = IteratorUtil.asCollection( schema.getIndexes( DoctorWhoLabels.Character ) );
        assertEquals( 1, indexes.size() );
        IndexDefinition firstIndex = indexes.iterator().next();
        assertEquals( "Character", firstIndex.getLabel().name() );
        assertEquals( "[character]", firstIndex.getPropertyKeys().toString() );
//        assertArrayEquals( new String[] {"character"}, IteratorUtil.asCollection( firstIndex.getPropertyKeys() ).toArray());
    }

    @Test
    public void shouldCreateSchemaIndexOnCharactersInCypher()
    {
        Schema schema = universe.getDatabase().schema();

        assertEquals( 0, IteratorUtil.count( schema.getIndexes( DoctorWhoLabels.Character ) ) );

        String cql = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql = "CREATE INDEX ON :Character(character)";

        // SNIPPET_END

        executionEngine.execute( cql );

        Collection<IndexDefinition> indexes = IteratorUtil.asCollection( schema.getIndexes( DoctorWhoLabels.Character ) );
        assertEquals( 1, indexes.size() );
        IndexDefinition firstIndex = indexes.iterator().next();
        assertEquals( "Character", firstIndex.getLabel().name() );
        assertEquals( "[character]", firstIndex.getPropertyKeys().toString() );
//        assertArrayEquals( new String[] {"character"}, IteratorUtil.asCollection( firstIndex.getPropertyKeys() ).toArray());
    }

}
