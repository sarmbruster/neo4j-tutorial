package org.neo4j.tutorial;

import static junit.framework.Assert.assertEquals;

import org.junit.*;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

public class AwesomenessRatingEngineTest
{

    private static EmbeddedDoctorWhoUniverse universe;
    private Transaction tx;

    @BeforeClass
    public static void createDatabase() throws Exception
    {
        universe = new EmbeddedDoctorWhoUniverse( new DoctorWhoUniverseGenerator() );
    }

    @AfterClass
    public static void closeTheDatabase()
    {
        universe.stop();
    }

    @Before
    public void openTransaction() {
        tx = universe.getDatabase().beginTx();
    }

    @After
    public void closeTransaction() {
        tx.close();
    }

    @Test
    public void shouldRateTheDoctorAs100PercentAwesome()
    {
        AwesomenessRatingEngine engine = new AwesomenessRatingEngine();
        assertEquals( 100.0, engine.rateAwesomeness( universe.getDatabase(), universe.theDoctor().getId() ) );
    }

    @Test
    public void shouldRateCompanionsAs50PercentAwesome()
    {
        Node rose = universe.getDatabase().index().forNodes( "characters" ).get( "character",
                "Rose Tyler" ).getSingle();

        AwesomenessRatingEngine engine = new AwesomenessRatingEngine();
        assertEquals( 50.0, engine.rateAwesomeness( universe.getDatabase(), rose.getId() ) );
    }


    @Test
    public void shouldRateEarthAs33PercentAwesome()
    {
        Node earth = universe.getDatabase().index().forNodes( "planets" ).get( "planet", "Earth" ).getSingle();

        AwesomenessRatingEngine engine = new AwesomenessRatingEngine();
        assertEquals( 33.3, engine.rateAwesomeness( universe.getDatabase(), earth.getId() ), 0.3 );
    }
}
