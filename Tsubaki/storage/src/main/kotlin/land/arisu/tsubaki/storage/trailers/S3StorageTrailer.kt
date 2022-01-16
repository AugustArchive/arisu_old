/**
 * ☔🥀 Tsubaki: Core backend infrastructure for Arisu, all the magic begins here ✨🚀
 * Copyright (C) 2020-2021 Noelware
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package land.arisu.tsubaki.storage.trailers

import java.io.InputStream
import java.net.URI
import land.arisu.tsubaki.common.kotlin.logging
import land.arisu.tsubaki.storage.IStorageTrailer
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.Bucket

/**
 * Represents a [storage trailer][IStorageTrailer] for AWS S3 support.
 * This includes using Wasabi if your S3 bucket is on there!
 *
 * @param useWasabi If [this storage trailer][IStorageTrailer] should be initialized using Wasabi S3.
 * @param accessKey The access key to use to authenticate with AWS.
 * @param secretKey The secret key to use to authenticate with AWS.
 * @param bucketName The bucket name to use (default: **tsubaki**)
 */
class S3StorageTrailer(
    private val useWasabi: Boolean,
    private val accessKey: String,
    private val secretKey: String,
    private val bucketName: String = "tsubaki"
): IStorageTrailer {
    override val name: String = "trailer:s3"
    private val logger by logging(this::class.java)

    private lateinit var bucket: Bucket
    private lateinit var client: S3Client

    override fun init() {
        logger.info("Initializing storage trailer...")
        val builder = S3Client.builder()
            .credentialsProvider(StaticCredentialsProvider.create(
                AwsSessionCredentials.create(
                    accessKey,
                    secretKey,
                    ""
                )
            ))
            .region(Region.US_EAST_1) // TODO: make this configurable

        if (useWasabi)
            builder.endpointOverride(URI.create("https://s3.wasabisys.com"))

        client = builder.build()
        logger.info("Created a new S3 client, validating buckets...")

        val buckets = client.listBuckets().buckets()
        val _bucket = buckets.find {
            it.name() == bucketName
        }

        if (_bucket == null) {
            logger.warn("Bucket with name \"$bucketName\" was not found, creating...")
            try {
                client.createBucket {
                    it.bucket(bucketName)
                }

                bucket = client.listBuckets().buckets().find { it.name() == bucketName }!!
                logger.info("Created bucket \"$bucketName\"!")
            } catch (e: Exception) {
                logger.warn("Unable to create S3 bucket: \"$bucketName\"")
                throw e
            }
        } else {
            logger.info("Using bucket \"$bucketName\".")
            bucket = _bucket
        }

        logger.info("✔ Initialized storage bucket with ${if (useWasabi) "Wasabi" else "AWS S3"}.")
    }

    override fun handle(files: List<InputStream>): Boolean {
        // TODO: this
        return true
    }
}
