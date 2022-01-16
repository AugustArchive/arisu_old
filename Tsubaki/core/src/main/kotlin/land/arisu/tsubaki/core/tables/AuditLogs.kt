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

package land.arisu.tsubaki.core.tables

import land.arisu.tsubaki.core.exposed.tables.SnowflakeEntity
import land.arisu.tsubaki.core.exposed.tables.SnowflakeEntityClass
import land.arisu.tsubaki.core.exposed.tables.SnowflakeTable
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.Column

/**
 * List of audit log actions available
 */
enum class AuditLogAction {
    // Project-related actions

    /**
     * The owner of the project added a member to view or edit the project
     */
    OWNER_ADDED_MEMBER,

    /**
     * The owner of the project added a member to view or edit the project
     */
    OWNER_REMOVED_MEMBER,

    /**
     * The project went private, so only the members who were
     * added to the project can see it.
     */
    PROJECT_WENT_PRIVATE,

    /**
     * The project went public again so everyone can see it!
     */
    PROJECT_WENT_PUBLIC,

    /**
     * The owner of the project added a webhook, you will get a `webhook_id`
     * attribute when you fetch the project audit logs.
     */
    OWNER_ADDED_WEBHOOK,

    /**
     * The owner of the project removed a webhook, you will get a `webhook_id`
     * attribute when you fetch the project audit logs.
     */
    OWNER_REMOVED_WEBHOOK,

    // Organization-related actions
    /**
     * Any organization member with the `CAN_ADD_MEMBERS` permission added a member
     * to the organization, you will get a `member_id` attribute when you fetch
     * the organization audit logs.
     */
    MEMBER_ADDED_TO_ORG,

    /**
     * Any organization member with the `CAN_ADD_MEMBERS` permission kicked a member
     * to the organization, you will get a `member_id` attribute when you fetch
     * the organization audit logs.
     */
    MEMBER_KICKED_FROM_ORG,

    /**
     * Any organization member with the `EDIT_MEMBER_PERMISSION` permission edited
     * another member's permission, you will get a `member_id` and a `permission_ids`
     * attribute when you fetch the organization audit logs.
     */
    MEMBER_UPDATED_PERMISSIONS
}

/**
 * Represents the target of the audit log action
 */
enum class AuditLogTarget {
    ORGANIZATION,
    PROJECT
}

/**
 * The table for the `audit_logs` table in PostgreSQL.
 */
object AuditLogsTable: SnowflakeTable(name = "audit_logs") {
    /**
     * A list of permission IDs available, this is only present in the [AuditLogTarget.ORGANIZATION] target.
     */
    val permissionIds: Column<Int?> = integer("permission_ids").nullable()

    /**
     * The [action][AuditLogAction] by it's enumeration ordinal, from 0 - 8
     */
    val actionType: Column<AuditLogAction> = enumeration("actionType", AuditLogAction::class)

    /**
     * The webhook ID that any action has performed. This is only when [AuditLogAction.OWNER_ADDED_WEBHOOK] and
     * [AuditLogAction.OWNER_REMOVED_WEBHOOK] was performed.
     */
    val webhookId: Column<String?> = text("webhook_id").nullable()

    /**
     * The member ID affected. This is only present in the [AuditLogAction.MEMBER_ADDED_TO_ORG], [AuditLogAction.MEMBER_KICKED_FROM_ORG],
     * [AuditLogAction.MEMBER_UPDATED_PERMISSIONS], [AuditLogAction.OWNER_ADDED_MEMBER], and [AuditLogAction.OWNER_REMOVED_MEMBER] targets.
     */
    val memberId: Column<String?> = text("member_id").nullable()

    /**
     * The project ID affected. This is only present in the [AuditLogAction.PROJECT_WENT_PRIVATE] and [AuditLogAction.PROJECT_WENT_PUBLIC] targets.
     */
    val project: Column<String?> = text("project").nullable()

    /**
     * The reason on why this audit log was inserted, used in the `X-Audit-Log-Reason` header.
     */
    val reason: Column<String?> = varchar("reason", 512).nullable()

    /**
     * The target enumeration ordinal.
     */
    val target: Column<AuditLogTarget> = enumeration("target", AuditLogTarget::class)
}

class AuditLogs(id: EntityID<String>): SnowflakeEntity(id) {
    companion object: SnowflakeEntityClass<AuditLogs>(AuditLogsTable)

    /**
     * A list of permission IDs available, this is only present in the [AuditLogTarget.ORGANIZATION] target.
     */
    var permissionIds by AuditLogsTable.permissionIds

    /**
     * The [action][AuditLogAction] by it's enumeration ordinal, from 0 - 8
     */
    var actionType by AuditLogsTable.actionType

    /**
     * The webhook ID that any action has performed. This is only when [AuditLogAction.OWNER_ADDED_WEBHOOK] and
     * [AuditLogAction.OWNER_REMOVED_WEBHOOK] was performed.
     */
    var webhookId by AuditLogsTable.webhookId

    /**
     * The member ID affected. This is only present in the [AuditLogAction.MEMBER_ADDED_TO_ORG], [AuditLogAction.MEMBER_KICKED_FROM_ORG],
     * [AuditLogAction.MEMBER_UPDATED_PERMISSIONS], [AuditLogAction.OWNER_ADDED_MEMBER], and [AuditLogAction.OWNER_REMOVED_MEMBER] targets.
     */
    var memberId by AuditLogsTable.memberId

    /**
     * The project ID affected. This is only present in the [AuditLogAction.PROJECT_WENT_PRIVATE] and [AuditLogAction.PROJECT_WENT_PUBLIC] targets.
     */
    var project by AuditLogsTable.project

    /**
     * The reason on why this audit log was inserted, used in the `X-Audit-Log-Reason` header.
     */
    var reason by AuditLogsTable.reason

    /**
     * The target enumeration ordinal.
     */
    var target by AuditLogsTable.target
}
