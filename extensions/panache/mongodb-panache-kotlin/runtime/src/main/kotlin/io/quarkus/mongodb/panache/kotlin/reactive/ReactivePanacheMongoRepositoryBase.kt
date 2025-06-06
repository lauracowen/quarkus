@file:Suppress("unused")

package io.quarkus.mongodb.panache.kotlin.reactive

import io.quarkus.mongodb.panache.kotlin.reactive.runtime.KotlinReactiveMongoOperations.Companion.INSTANCE
import io.quarkus.mongodb.reactive.ReactiveMongoCollection
import io.quarkus.mongodb.reactive.ReactiveMongoDatabase
import io.quarkus.panache.common.Parameters
import io.quarkus.panache.common.Sort
import io.quarkus.panache.common.impl.GenerateBridge
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import java.util.stream.Stream
import org.bson.conversions.Bson

/**
 * Represents a Repository for a specific type of entity [Entity], with an ID type of [Id].
 * Implementing this repository will gain you the exact same useful methods that are on
 * [ReactivePanacheMongoEntityBase]. Unless you have a custom ID strategy, you should not implement
 * this interface directly but implement [ReactivePanacheMongoRepository] instead.
 *
 * @param Entity The type of entity to operate on
 * @param Id The ID type of the entity
 * @see [ReactivePanacheMongoRepository]
 */
interface ReactivePanacheMongoRepositoryBase<Entity : Any, Id : Any> {
    /**
     * Persist the given entity in the database. This will set its ID field if not already set.
     *
     * @param entity the entity to insert.
     */
    fun persist(entity: Entity): Uni<Entity> = INSTANCE.persist(entity).map { entity }

    /**
     * Update the given entity in the database.
     *
     * @param entity the entity to update.
     */
    fun update(entity: Entity): Uni<Entity> = INSTANCE.update(entity).map { entity }

    /**
     * Persist the given entity in the database or update it if it already exists.
     *
     * @param entity the entity to update.
     */
    fun persistOrUpdate(entity: Entity): Uni<Entity> =
        INSTANCE.persistOrUpdate(entity).map { entity }

    /**
     * Delete the given entity from the database, if it is already persisted.
     *
     * @param entity the entity to delete.
     * @see [deleteAll]
     */
    fun delete(entity: Entity): Uni<Void> = INSTANCE.delete(entity)

    /**
     * Find an entity of this type by ID.
     *
     * @param id the ID of the entity to find.
     * @return the entity found, or `null` if not found.
     */
    @GenerateBridge
    fun findById(id: Id): Uni<Entity?> = throw INSTANCE.implementationInjectionMissing()

    /**
     * Find entities using a query, with optional indexed parameters.
     *
     * @param query a query string
     * @param params optional sequence of indexed parameters
     * @return a new [ReactivePanacheQuery] instance for the given query
     * @see [list]
     * @see [stream]
     */
    @GenerateBridge
    fun find(query: String, vararg params: Any?): ReactivePanacheQuery<Entity> =
        throw INSTANCE.implementationInjectionMissing()

    /**
     * Find entities using a query and the given sort options, with optional indexed parameters.
     *
     * @param query a query string
     * @param sort the sort strategy to use
     * @param params optional sequence of indexed parameters
     * @return a new [ReactivePanacheQuery] instance for the given query
     * @see [list]
     * @see [stream]
     */
    @GenerateBridge
    fun find(query: String, sort: Sort, vararg params: Any?): ReactivePanacheQuery<Entity> =
        throw INSTANCE.implementationInjectionMissing()

    /**
     * Find entities using a query, with named parameters.
     *
     * @param query a query string
     * @param params [Map] of named parameters
     * @return a new [ReactivePanacheQuery] instance for the given query
     * @see [list]
     * @see [stream]
     */
    @GenerateBridge
    fun find(query: String, params: Map<String, Any?>): ReactivePanacheQuery<Entity> =
        throw INSTANCE.implementationInjectionMissing()

    /**
     * Find entities using a query and the given sort options, with named parameters.
     *
     * @param query a query string
     * @param sort the sort strategy to use
     * @param params [Map] of indexed parameters
     * @return a new [ReactivePanacheQuery] instance for the given query
     * @see [list]
     * @see [stream]
     */
    @GenerateBridge
    fun find(query: String, sort: Sort, params: Map<String, Any?>): ReactivePanacheQuery<Entity> =
        throw INSTANCE.implementationInjectionMissing()

    /**
     * Find entities using a query, with named parameters.
     *
     * @param query a query string
     * @param params [Parameters] of named parameters
     * @return a new [ReactivePanacheQuery] instance for the given query
     * @see [list]
     * @see [stream]
     */
    @GenerateBridge
    fun find(query: String, params: Parameters): ReactivePanacheQuery<Entity> =
        throw INSTANCE.implementationInjectionMissing()

    /**
     * Find entities using a query and the given sort options, with named parameters.
     *
     * @param query a query string
     * @param sort the sort strategy to use
     * @param params [Parameters] of indexed parameters
     * @return a new [ReactivePanacheQuery] instance for the given query
     * @see [list]
     * @see [stream]
     */
    @GenerateBridge
    fun find(query: String, sort: Sort, params: Parameters): ReactivePanacheQuery<Entity> =
        throw INSTANCE.implementationInjectionMissing()

    /**
     * Find entities using a BSON query.
     *
     * @param query a [Bson] query
     * @return a new [ReactivePanacheQuery] instance for the given query
     * @see [list]
     * @see [stream]
     */
    @GenerateBridge
    fun find(query: Bson): ReactivePanacheQuery<Entity> =
        throw INSTANCE.implementationInjectionMissing()

    /**
     * Find entities using a BSON query and a BSON sort.
     *
     * @param query a [Bson] query
     * @param sort the [Bson] sort
     * @return a new [ReactivePanacheQuery] instance for the given query
     * @see [list]
     * @see [stream]
     */
    @GenerateBridge
    fun find(query: Bson, sort: Bson): ReactivePanacheQuery<Entity> =
        throw INSTANCE.implementationInjectionMissing()

    /**
     * Find all entities of this type.
     *
     * @return a new [ReactivePanacheQuery] instance to find all entities of this type.
     * @see [listAll]
     * @see [streamAll]
     */
    @GenerateBridge
    fun findAll(): ReactivePanacheQuery<Entity> = throw INSTANCE.implementationInjectionMissing()

    /**
     * Find all entities of this type, in the given order.
     *
     * @param sort the sort order to use
     * @return a new [ReactivePanacheQuery] instance to find all entities of this type.
     * @see [listAll]
     * @see [streamAll]
     */
    @GenerateBridge
    fun findAll(sort: Sort): ReactivePanacheQuery<Entity> =
        throw INSTANCE.implementationInjectionMissing()

    /**
     * Find entities matching a query, with optional indexed parameters. This method is a shortcut
     * for `find(query, params).list()`.
     *
     * @param query a query string
     * @param params optional sequence of indexed parameters
     * @return a [List] containing all results, without paging
     * @see [find]
     * @see [stream]
     */
    @GenerateBridge
    fun list(query: String, vararg params: Any?): Uni<List<Entity>> =
        throw INSTANCE.implementationInjectionMissing()

    /**
     * Find entities matching a query and the given sort options, with optional indexed parameters.
     * This method is a shortcut for `find(query, sort, params).list()`.
     *
     * @param query a query string
     * @param sort the sort strategy to use
     * @param params optional sequence of indexed parameters
     * @return a [List] containing all results, without paging
     * @see [find]
     * @see [stream]
     */
    @GenerateBridge
    fun list(query: String, sort: Sort, vararg params: Any?): Uni<List<Entity>> =
        throw INSTANCE.implementationInjectionMissing()

    /**
     * Find entities matching a query, with named parameters. This method is a shortcut for
     * `find(query, params).list()`.
     *
     * @param query a query string
     * @param params [Map] of named parameters
     * @return a [List] containing all results, without paging
     * @see [find]
     * @see [stream]
     */
    @GenerateBridge
    fun list(query: String, params: Map<String, Any?>): Uni<List<Entity>> =
        throw INSTANCE.implementationInjectionMissing()

    /**
     * Find entities matching a query and the given sort options, with named parameters. This method
     * is a shortcut for `find(query, sort, params).list()`.
     *
     * @param query a query string
     * @param sort the sort strategy to use
     * @param params [Map] of indexed parameters
     * @return a [List] containing all results, without paging
     * @see [find]
     * @see [stream]
     */
    @GenerateBridge
    fun list(query: String, sort: Sort, params: Map<String, Any?>): Uni<List<Entity>> =
        throw INSTANCE.implementationInjectionMissing()

    /**
     * Find entities matching a query, with named parameters. This method is a shortcut for
     * `find(query, params).list()`.
     *
     * @param query a query string
     * @param params [Parameters] of named parameters
     * @return a [List] containing all results, without paging
     * @see [find]
     * @see [stream]
     */
    @GenerateBridge
    fun list(query: String, params: Parameters): Uni<List<Entity>> =
        throw INSTANCE.implementationInjectionMissing()

    /**
     * Find entities matching a query and the given sort options, with named parameters. This method
     * is a shortcut for `find(query, sort, params).list()`.
     *
     * @param query a query string
     * @param sort the sort strategy to use
     * @param params [Parameters] of indexed parameters
     * @return a [List] containing all results, without paging
     * @see [find]
     * @see [stream]
     */
    @GenerateBridge
    fun list(query: String, sort: Sort, params: Parameters): Uni<List<Entity>> =
        throw INSTANCE.implementationInjectionMissing()

    /**
     * Find entities using a BSON query. This method is a shortcut for `find(query).list()`.
     *
     * @param query a [Bson] query
     * @return a [List] containing all results, without paging
     * @see [find]
     * @see [stream]
     */
    @GenerateBridge
    fun list(query: Bson): Uni<List<Entity>> = throw INSTANCE.implementationInjectionMissing()

    /**
     * Find entities using a BSON query and a BSON sort. This method is a shortcut for `find(query,
     * sort).list()`.
     *
     * @param query a [Bson] query
     * @param sort the [Bson] sort
     * @return a [List] containing all results, without paging
     * @see [find]
     * @see [stream]
     */
    @GenerateBridge
    fun list(query: Bson, sort: Bson): Uni<List<Entity>> =
        throw INSTANCE.implementationInjectionMissing()

    /**
     * Find all entities of this type. This method is a shortcut for `findAll().list()`.
     *
     * @return a [List] containing all results, without paging
     * @see [findAll]
     * @see [streamAll]
     */
    @GenerateBridge
    fun listAll(): Uni<List<Entity>> = throw INSTANCE.implementationInjectionMissing()

    /**
     * Find all entities of this type, in the given order. This method is a shortcut for
     * `findAll(sort).list()`.
     *
     * @param sort the sort order to use
     * @return a [List] containing all results, without paging
     * @see [findAll]
     * @see [streamAll]
     */
    @GenerateBridge
    fun listAll(sort: Sort): Uni<List<Entity>> = throw INSTANCE.implementationInjectionMissing()

    /**
     * Find entities matching a query, with optional indexed parameters. This method is a shortcut
     * for `find(query, params).stream()`.
     *
     * @param query a query string
     * @param params optional sequence of indexed parameters
     * @return a [Multi] containing all results, without paging
     * @see [find]
     * @see [list]
     */
    @GenerateBridge
    fun stream(query: String, vararg params: Any?): Multi<Entity> =
        throw INSTANCE.implementationInjectionMissing()

    /**
     * Find entities matching a query and the given sort options, with optional indexed parameters.
     * This method is a shortcut for `find(query, sort, params).stream()`.
     *
     * @param query a query string
     * @param sort the sort strategy to use
     * @param params optional sequence of indexed parameters
     * @return a [Multi] containing all results, without paging
     * @see [find]
     * @see [list]
     */
    @GenerateBridge
    fun stream(query: String, sort: Sort, vararg params: Any?): Multi<Entity> =
        throw INSTANCE.implementationInjectionMissing()

    /**
     * Find entities matching a query, with named parameters. This method is a shortcut for
     * `find(query, params).stream()`.
     *
     * @param query a query string
     * @param params [Map] of named parameters
     * @return a [Multi] containing all results, without paging
     * @see [find]
     * @see [list]
     */
    @GenerateBridge
    fun stream(query: String, params: Map<String, Any?>): Multi<Entity> =
        throw INSTANCE.implementationInjectionMissing()

    /**
     * Find entities matching a query and the given sort options, with named parameters. This method
     * is a shortcut for `find(query, sort, params).stream()`.
     *
     * @param query a query string
     * @param sort the sort strategy to use
     * @param params [Map] of indexed parameters
     * @return a [Multi] containing all results, without paging
     * @see [find]
     * @see [list]
     */
    @GenerateBridge
    fun stream(query: String, sort: Sort, params: Map<String, Any?>): Multi<Entity> =
        throw INSTANCE.implementationInjectionMissing()

    /**
     * Find entities matching a query, with named parameters. This method is a shortcut for
     * `find(query, params).stream()`.
     *
     * @param query a query string
     * @param params [Parameters] of named parameters
     * @return a [Multi] containing all results, without paging
     * @see [find]
     * @see [list]
     */
    @GenerateBridge
    fun stream(query: String, params: Parameters): Multi<Entity> =
        throw INSTANCE.implementationInjectionMissing()

    /**
     * Find entities matching a query and the given sort options, with named parameters. This method
     * is a shortcut for `find(query, sort, params).stream()`.
     *
     * @param query a query string
     * @param sort the sort strategy to use
     * @param params [Parameters] of indexed parameters
     * @return a [Multi] containing all results, without paging
     * @see [find]
     * @see [list]
     */
    @GenerateBridge
    fun stream(query: String, sort: Sort, params: Parameters): Multi<Entity> =
        throw INSTANCE.implementationInjectionMissing()

    /**
     * Find entities using a BSON query. This method is a shortcut for `find(query).stream()`.
     *
     * @param query a [Bson] query
     * @return a [Multi] containing all results, without paging
     * @see [find]
     * @see [list]
     */
    @GenerateBridge
    fun stream(query: Bson): Multi<Entity> = throw INSTANCE.implementationInjectionMissing()

    /**
     * Find entities using a BSON query and a BSON sort. This method is a shortcut for `find(query,
     * sort).stream()`.
     *
     * @param query a [Bson] query
     * @param sort the [Bson] sort
     * @return a [Multi] containing all results, without paging
     * @see [find]
     * @see [list]
     */
    @GenerateBridge
    fun stream(query: Bson, sort: Bson): Multi<Entity> =
        throw INSTANCE.implementationInjectionMissing()

    /**
     * Find all entities of this type. This method is a shortcut for `findAll().stream()`.
     *
     * @return a [Multi] containing all results, without paging
     * @see [findAll]
     * @see [listAll]
     */
    @GenerateBridge
    fun streamAll(sort: Sort): Multi<Entity> = throw INSTANCE.implementationInjectionMissing()

    /**
     * Find all entities of this type, in the given order. This method is a shortcut for
     * `findAll(sort).stream()`.
     *
     * @return a [Multi] containing all results, without paging
     * @see [findAll]
     * @see [listAll]
     */
    @GenerateBridge fun streamAll(): Multi<Entity> = throw INSTANCE.implementationInjectionMissing()

    /**
     * Counts the number of this type of entity in the database.
     *
     * @return the number of this type of entity in the database.
     */
    @GenerateBridge fun count(): Uni<Long> = throw INSTANCE.implementationInjectionMissing()

    /**
     * Counts the number of this type of entity matching the given query, with optional indexed
     * parameters.
     *
     * @param query a query string
     * @param params optional sequence of indexed parameters
     * @return the number of entities counted.
     */
    @GenerateBridge
    fun count(query: String, vararg params: Any?): Uni<Long> =
        throw INSTANCE.implementationInjectionMissing()

    /**
     * Counts the number of this type of entity matching the given query, with named parameters.
     *
     * @param query a query string
     * @param params [Map] of named parameters
     * @return the number of entities counted.
     */
    @GenerateBridge
    fun count(query: String, params: Map<String, Any?>): Uni<Long> =
        throw INSTANCE.implementationInjectionMissing()

    /**
     * Counts the number of this type of entity matching the given query, with named parameters.
     *
     * @param query a query string
     * @param params [Parameters] of named parameters
     * @return the number of entities counted.
     */
    @GenerateBridge
    fun count(query: String, params: Parameters): Uni<Long> =
        throw INSTANCE.implementationInjectionMissing()

    /**
     * Counts the number of this type of entity matching the given query
     *
     * @param query a [Bson] query
     * @return the number of entities counted.
     */
    @GenerateBridge
    fun count(query: Bson): Uni<Long> = throw INSTANCE.implementationInjectionMissing()

    /**
     * Delete all entities of this type from the database.
     *
     * @return the number of entities deleted.
     * @see [delete]
     */
    @GenerateBridge fun deleteAll(): Uni<Long> = throw INSTANCE.implementationInjectionMissing()

    /**
     * Delete an entity of this type by ID.
     *
     * @param id the ID of the entity to delete.
     * @return false if the entity was not deleted (not found).
     */
    @GenerateBridge
    fun deleteById(id: Id): Uni<Boolean> = throw INSTANCE.implementationInjectionMissing()

    /**
     * Delete all entities of this type matching the given query, with optional indexed parameters.
     *
     * @param query a query string
     * @param params optional sequence of indexed parameters
     * @return the number of entities deleted.
     * @see [deleteAll]
     */
    @GenerateBridge
    fun delete(query: String, vararg params: Any?): Uni<Long> =
        throw INSTANCE.implementationInjectionMissing()

    /**
     * Delete all entities of this type matching the given query, with named parameters.
     *
     * @param query a query string
     * @param params [Map] of named parameters
     * @return the number of entities deleted.
     * @see [deleteAll]
     */
    @GenerateBridge
    fun delete(query: String, params: Map<String, Any?>): Uni<Long> =
        throw INSTANCE.implementationInjectionMissing()

    /**
     * Delete all entities of this type matching the given query, with named parameters.
     *
     * @param query a query string
     * @param params [Parameters] of named parameters
     * @return the number of entities deleted.
     * @see [deleteAll]
     */
    @GenerateBridge
    fun delete(query: String, params: Parameters): Uni<Long> =
        throw INSTANCE.implementationInjectionMissing()

    /**
     * Delete all entities of this type matching the given query
     *
     * @param query a [Bson] query
     * @return the number of entities counted.
     */
    @GenerateBridge
    fun delete(query: Bson): Uni<Long> = throw INSTANCE.implementationInjectionMissing()

    /**
     * Persist all given entities.
     *
     * @param entities the entities to insert
     */
    fun persist(entities: Iterable<Entity>): Uni<Void> = INSTANCE.persist(entities)

    /**
     * Persist all given entities.
     *
     * @param entities the entities to insert
     */
    fun persist(entities: Stream<Entity>): Uni<Void> = INSTANCE.persist(entities)

    /**
     * Persist all given entities.
     *
     * @param entities the entities to insert
     */
    fun persist(firstEntity: Entity, vararg entities: Entity): Uni<Void> =
        INSTANCE.persist(firstEntity, *entities)

    /**
     * Update all given entities.
     *
     * @param entities the entities to update
     */
    fun update(entities: Iterable<Entity>): Uni<Void> = INSTANCE.update(entities)

    /**
     * Update all given entities.
     *
     * @param entities the entities to update
     */
    fun update(entities: Stream<Entity>): Uni<Void> = INSTANCE.update(entities)

    /**
     * Update all given entities.
     *
     * @param entities the entities to update
     */
    fun update(firstEntity: Entity, vararg entities: Entity): Uni<Void> =
        INSTANCE.update(firstEntity, *entities)

    /**
     * Persist all given entities or update them if they already exist.
     *
     * @param entities the entities to update
     */
    fun persistOrUpdate(entities: Iterable<Entity>): Uni<Void> = INSTANCE.persistOrUpdate(entities)

    /**
     * Persist all given entities or update them if they already exist.
     *
     * @param entities the entities to update
     */
    fun persistOrUpdate(entities: Stream<Entity>): Uni<Void> = INSTANCE.persistOrUpdate(entities)

    /**
     * Persist all given entities or update them if they already exist.
     *
     * @param entities the entities to update
     */
    fun persistOrUpdate(firstEntity: Entity, vararg entities: Entity): Uni<Void> =
        INSTANCE.persistOrUpdate(firstEntity, *entities)

    /**
     * Update all entities of this type by the given update document, with optional indexed
     * parameters. The returned [io.quarkus.mongodb.panache.common.reactive.ReactivePanacheUpdate]
     * object will allow to restrict on which document the update should be applied.
     *
     * @param update the update document, if it didn't contain any update operator, we add `$set`.
     *   It can also be expressed as a query string.
     * @param params optional sequence of indexed parameters
     * @return a new [io.quarkus.mongodb.panache.common.reactive.ReactivePanacheUpdate] instance for
     *   the given update document
     */
    @GenerateBridge
    fun update(
        update: String,
        vararg params: Any?,
    ): io.quarkus.mongodb.panache.common.reactive.ReactivePanacheUpdate =
        throw INSTANCE.implementationInjectionMissing()

    /**
     * Update all entities of this type by the given update document, with named parameters. The
     * returned [io.quarkus.mongodb.panache.common.reactive.ReactivePanacheUpdate] object will allow
     * to restrict on which document the update should be applied.
     *
     * @param update the update document, if it didn't contain any update operator, we add `$set`.
     *   It can also be expressed as a query string.
     * @param params [Map] of named parameters
     * @return a new [io.quarkus.mongodb.panache.common.reactive.ReactivePanacheUpdate] instance for
     *   the given update document
     */
    @GenerateBridge
    fun update(
        update: String,
        params: Map<String, Any?>,
    ): io.quarkus.mongodb.panache.common.reactive.ReactivePanacheUpdate =
        throw INSTANCE.implementationInjectionMissing()

    /**
     * Update all entities of this type by the given update document, with named parameters. The
     * returned [io.quarkus.mongodb.panache.common.reactive.ReactivePanacheUpdate] object will allow
     * to restrict on which document the update should be applied.
     *
     * @param update the update document, if it didn't contain any update operator, we add `$set`.
     *   It can also be expressed as a query string.
     * @param params [Parameters] of named parameters
     * @return a new [io.quarkus.mongodb.panache.common.reactive.ReactivePanacheUpdate] instance for
     *   the given update document
     */
    @GenerateBridge
    fun update(
        update: String,
        params: Parameters,
    ): io.quarkus.mongodb.panache.common.reactive.ReactivePanacheUpdate =
        throw INSTANCE.implementationInjectionMissing()

    /**
     * Update all entities of this type by the given BSON update document. The returned
     * [io.quarkus.mongodb.panache.common.reactive.ReactivePanacheUpdate] object will allow to
     * restrict on which document the update should be applied.
     *
     * @param update the update document, as a [Bson].
     * @param params [Parameters] of named parameters
     * @return a new [io.quarkus.mongodb.panache.common.reactive.ReactivePanacheUpdate] instance for
     *   the given update document
     */
    @GenerateBridge
    fun update(update: Bson): io.quarkus.mongodb.panache.common.reactive.ReactivePanacheUpdate =
        throw INSTANCE.implementationInjectionMissing()

    /** Allow to access the underlying Mongo Collection */
    @GenerateBridge
    fun mongoCollection(): ReactiveMongoCollection<Entity> =
        throw INSTANCE.implementationInjectionMissing()

    /** Allow to access the underlying Mongo Database. */
    @GenerateBridge
    fun mongoDatabase(): ReactiveMongoDatabase = throw INSTANCE.implementationInjectionMissing()
}
