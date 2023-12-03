package controllers

import models.Vinyl
import models.Collection
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import persistence.JSONSerializer
import persistence.XMLSerializer
import persistence.YamlSerializer
import java.io.File

class CollectionAPITest {

    private var phoebeBridgers: Collection? = null
    private var summerVibes: Collection? = null
    private var sadAlbums: Collection? = null

    private var populatedCollections: CollectionAPI? = CollectionAPI(YamlSerializer(File("collections.yaml")))
    private var emptyCollections: CollectionAPI? = CollectionAPI(YamlSerializer(File("collections.yaml")))

    @BeforeEach
    fun setup() {
        phoebeBridgers = Collection(collectionName = "Phoebe Bridgers Vinyls", isCollectionArchived = true)
        summerVibes = Collection(collectionName = "Summer Vibes", isCollectionArchived = false)
        sadAlbums = Collection(collectionName = "Rainy Day Albums", isCollectionArchived = false)

        populatedCollections!!.add(phoebeBridgers!!)
        populatedCollections!!.add(summerVibes!!)
        populatedCollections!!.add(sadAlbums!!)

    }

    @AfterEach
    fun tearDown() {
        phoebeBridgers = null
        summerVibes = null
        sadAlbums = null
        populatedCollections = null
        emptyCollections = null
    }

    @Nested
    inner class AddCollections {

        @Test
        fun `adding a collection to a populated list adds to ArrayList`() {
            val newCollection = Collection(collectionName = "New Vinyls", isCollectionArchived = false)
            assertEquals(3, populatedCollections!!.numberOfCollections())
            assertTrue(populatedCollections!!.add(newCollection))
            assertEquals(4, populatedCollections!!.numberOfCollections())
            assertEquals(newCollection, populatedCollections!!.findCollection(populatedCollections!!.numberOfCollections() - 1))
        }

        @Test
        fun `adding a collection to an empty list adds to ArrayList`() {
            val newCollection = Collection(collectionName = "New Vinyls", isCollectionArchived = false)
            assertEquals(0, emptyCollections!!.numberOfCollections())
            assertTrue(emptyCollections!!.add(newCollection))
            assertEquals(1, emptyCollections!!.numberOfCollections())
            assertEquals(newCollection, emptyCollections!!.findCollection(emptyCollections!!.numberOfCollections() - 1))
        }

    }

    @Nested
    inner class ListCollections {

        @Test
        fun `listAllCollections returns No Collections Stored message when ArrayList is empty`() {
            assertEquals(0, emptyCollections!!.numberOfCollections())
            assertTrue(emptyCollections!!.listAllCollections().lowercase().contains("no collections"))
        }

        @Test
        fun `listAllCollections returns Collections when ArrayList has collections stored`() {
            assertEquals(3, populatedCollections!!.numberOfCollections())
            val collectionsString = populatedCollections!!.listAllCollections().lowercase()
            assertFalse(collectionsString.contains("Phoebe Bridgers Vinyls"))
            assertFalse(collectionsString.contains("Summer Vibes"))
            assertFalse(collectionsString.contains("Rainy Day Albums"))
        }

        @Test
        fun `listActiveCollections returns no active collections stored when ArrayList is empty`() {
            assertEquals(0, emptyCollections!!.numberOfActiveCollections())
            assertTrue(
                emptyCollections!!.listActiveCollections().lowercase().contains("no active collections")
            )
        }

        @Test
        fun `listActiveCollections returns active collections when ArrayList has active notes stored`() {
            assertEquals(2, populatedCollections!!.numberOfActiveCollections())
            val activeCollectionsString = populatedCollections!!.listActiveCollections().lowercase()
            assertFalse(activeCollectionsString.contains("Phoebe Bridgers Vinyls"))
            assertFalse(activeCollectionsString.contains("Summer Vibes"))
            assertFalse(activeCollectionsString.contains("Rainy Day Albums"))
        }

        @Test
        fun `listArchivedCollections returns no archived collections when ArrayList is empty`() {
            assertEquals(0, emptyCollections!!.numberOfArchivedCollections())
            assertFalse(
                emptyCollections!!.listArchivedCollections().lowercase().contains("no archived notes")
            )
        }

        @Test
        fun `listArchivedCollections returns archived collections when ArrayList has archived notes stored`() {
            assertEquals(1, populatedCollections!!.numberOfArchivedCollections())
            val archivedCollectionsString = populatedCollections!!.listArchivedCollections().lowercase()
            assertFalse(archivedCollectionsString.contains("Phoebe Bridgers Vinyls"))
            assertFalse(archivedCollectionsString.contains("Summer Vibes"))
            assertFalse(archivedCollectionsString.contains("Rainy Day Albums"))
        }

    }

    @Nested
    inner class DeleteCollections {

        @Test
        fun `deleting a Collection that does not exist, returns null`() {
            assertNull(emptyCollections!!.delete(-1))
            assertNull(populatedCollections!!.delete(-1))
            assertNull(populatedCollections!!.delete(5))
        }

        @Test
        fun `deleting a Collection that exists delete and returns deleted object`() {
            assertEquals(3, populatedCollections!!.numberOfCollections())
            assertEquals(phoebeBridgers, populatedCollections!!.delete(4))
            assertEquals(2, populatedCollections!!.numberOfCollections())
            assertEquals(summerVibes, populatedCollections!!.delete(0))
            assertEquals(1, populatedCollections!!.numberOfCollections())
        }
    }

    @Nested
    inner class UpdateCollections {
        @Test
        fun `updating a collection that does not exist returns false`(){
            assertFalse(populatedCollections!!.update(6, Collection(collectionName = "New Releases", isCollectionArchived = false)))
            assertFalse(populatedCollections!!.update(-1, Collection(collectionName = "New Releases", isCollectionArchived = false)))
            assertFalse(emptyCollections!!.update(0, Collection(collectionName = "New Releases", isCollectionArchived = false)))
        }

    }

    @Nested
    inner class PersistenceTests {

        @Test
        fun `saving and loading an empty collection in YAML doesn't crash app`() {

            val storingCollections = CollectionAPI(YamlSerializer(File("collections.yaml")))
            storingCollections.store()

            //Loading the empty notes.xml file into a new object
            val loadedCollections = CollectionAPI(YamlSerializer(File("collections.yaml")))
            loadedCollections.load()

            //Comparing the source of the notes (storingNotes) with the XML loaded notes (loadedNotes)
            assertEquals(0, storingCollections.numberOfCollections())
            assertEquals(0, loadedCollections.numberOfCollections())
            assertEquals(storingCollections.numberOfCollections(), loadedCollections.numberOfCollections())
        }

        @Test
        fun `saving and loading an loaded collection in YAML doesn't loose data`() {
            // Storing 3 notes to the notes.XML file.
            val storingCollections = CollectionAPI(YamlSerializer(File("collections.yaml")))
            storingCollections.add(phoebeBridgers!!)
            storingCollections.add(summerVibes!!)
            storingCollections.add(sadAlbums!!)
            storingCollections.store()

            //Loading notes.xml into a different collection
            val loadedCollections = CollectionAPI(YamlSerializer(File("collections.yaml")))
            loadedCollections.load()

            //Comparing the source of the notes (storingNotes) with the XML loaded notes (loadedNotes)
            assertEquals(3, storingCollections.numberOfCollections())
            assertEquals(3, loadedCollections.numberOfCollections())
            assertEquals(storingCollections.numberOfCollections(), loadedCollections.numberOfCollections())
            assertEquals(storingCollections.findCollection(1), loadedCollections.findCollection(0))
            assertEquals(storingCollections.findCollection(2), loadedCollections.findCollection(1))
            assertEquals(storingCollections.findCollection(3), loadedCollections.findCollection(2))
        }

    }

    @Nested
    inner class ArchiveCollections {
        @Test
        fun `archiving a note that does not exist returns false`(){
            assertFalse(populatedCollections!!.archiveCollection(6))
            assertFalse(populatedCollections!!.archiveCollection(-1))
            assertFalse(emptyCollections!!.archiveCollection(0))
        }

        @Test
        fun `archiving an already archived note returns false`(){
            assertFalse(populatedCollections!!.findCollection(2)!!.isCollectionArchived)
            assertTrue(populatedCollections!!.archiveCollection(2))
        }

        @Test
        fun `archiving an active note that exists returns true and archives`() {
            assertFalse(populatedCollections!!.findCollection(1)!!.isCollectionArchived)
            assertTrue(populatedCollections!!.archiveCollection(1))
            assertTrue(populatedCollections!!.findCollection(1)!!.isCollectionArchived)
        }
    }

    @Nested
    inner class CountingMethods {

        @Test
        fun numberOfCollectionsCalculatedCorrectly() {
            assertEquals(3, populatedCollections!!.numberOfCollections())
            assertEquals(0, emptyCollections!!.numberOfCollections())
        }

        @Test
        fun numberOfArchivedCollectionsCalculatedCorrectly() {
            assertEquals(1, populatedCollections!!.numberOfArchivedCollections())
            assertEquals(0, emptyCollections!!.numberOfArchivedCollections())
        }

        @Test
        fun numberOfActiveCollectionsCalculatedCorrectly() {
            assertEquals(2, populatedCollections!!.numberOfActiveCollections())
            assertEquals(0, emptyCollections!!.numberOfActiveCollections())
        }

    }

    @Nested
    inner class SearchMethods {

        @Test
        fun `search notes by title returns no notes when no notes with that title exist`() {
            //Searching a populated collection for a title that doesn't exist.
            assertEquals(3, populatedCollections!!.numberOfCollections())
            val searchResults = populatedCollections!!.searchCollectionByTitle("no results expected")
            assertTrue(searchResults.isEmpty())

            //Searching an empty collection
            assertEquals(0, emptyCollections!!.numberOfCollections())
            assertTrue(emptyCollections!!.searchCollectionByTitle("").isEmpty())
        }

        @Test
        fun `search notes by title returns notes when notes with that title exist`() {
            assertEquals(3, populatedCollections!!.numberOfCollections())

            //Searching a populated collection for a full title that exists (case matches exactly)
            var searchResults = populatedCollections!!.searchCollectionByTitle("phoebe")
            assertTrue(searchResults.contains("Phoebe Bridgers Vinyls"))

            //Searching a populated collection for a partial title that exists (case matches exactly)
            searchResults = populatedCollections!!.searchCollectionByTitle("S")
            assertTrue(searchResults.contains("Summer Vibes"))

        }
    }
}