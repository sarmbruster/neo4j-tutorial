package org.neo4j.tutorial;

import java.util.Collections;
import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterable;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.helpers.collection.IteratorUtil;
import org.neo4j.tooling.GlobalGraphOperations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * In this Koan we explore the new features on Neo4j: Labels, Schema Indexes and Constraints
 */
public class Koan10a
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
    public void shouldCreateANodeWithLabelPersonInJava()
    {

        assertEquals( 0, countNodesForLabel( "Person" ) );
        Node node = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        node = universe.getDatabase().createNode();
        node.addLabel( DynamicLabel.label( "Person" ) );

        // SNIPPET_END

        assertEquals( 1, countNodesForLabel( "Person" ) );

    }

    @Test
    public void shouldCreateANodeWithLabelPersonInCypher()
    {

        String cql = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql = "CREATE (n:Person)";

        // SNIPPET_END

        executionEngine.execute( cql );

        // verify
        long count = (Long) IteratorUtil.first( executionEngine.execute( "MATCH (n:Person) RETURN count(n) as c" )
                .columnAs( "c" ) );
        assertEquals( 1, count );

    }

    @Test
    public void shouldFindAllTimeLordsInJava()
    {
        ResourceIterable<Node> timelords = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        timelords = GlobalGraphOperations.at( universe.getDatabase() )
                .getAllNodesWithLabel( DynamicLabel.label( "Timelord" ) );

        // SNIPPET_END

        assertEquals( 9, IteratorUtil.count( timelords ) );

    }

    @Test
    public void shouldFindAllTimelordsInCypher()
    {
        String cql = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql = "MATCH (t:Timelord) RETURN t";

        // SNIPPET_END
        assertEquals( 9, IteratorUtil.count( executionEngine.execute( cql ) ) );

    }

    @Test
    public void shouldFindTheDoctorWithLabelAndCharacterInJava()
    {
        Node theDoctor = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        ResourceIterable<Node> result = universe.getDatabase().findNodesByLabelAndProperty(
                DynamicLabel.label( "Timelord" ),
                "character",
                "Doctor" );
        theDoctor = IteratorUtil.single( result );

        // SNIPPET_END

        assertNotNull( theDoctor );

    }

    @Test
    public void shouldFindTheDoctorWithLabelAndCharacterInCypher()
    {
        String cql = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql = "MATCH (t:Timelord) WHERE t.character='Doctor' RETURN t";

        // SNIPPET_END

        assertNotNull( IteratorUtil.single( executionEngine.execute( cql ) ) );
    }

    private int countNodesForLabel( String labelName )
    {
        ResourceIterable<Node> nodesWithLabelIterable = GlobalGraphOperations.at( universe.getDatabase() )
                .getAllNodesWithLabel( DynamicLabel.label( labelName ) );
        return IteratorUtil.count( nodesWithLabelIterable );
    }


}
