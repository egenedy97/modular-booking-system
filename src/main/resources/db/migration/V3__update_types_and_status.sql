-- Step 1: Drop old constraints
ALTER TABLE notification DROP CONSTRAINT IF EXISTS notification_type_check;
ALTER TABLE notification DROP CONSTRAINT IF EXISTS notification_status_check;

-- Step 2: Convert type column safely
ALTER TABLE notification ALTER COLUMN type TYPE VARCHAR(50) USING
    CASE
        WHEN type::TEXT = '0' THEN 'EMAIL'
        WHEN type::TEXT = '1' THEN 'SMS'
        WHEN type::TEXT = '2' THEN 'MMS'
        WHEN type IN ('EMAIL','SMS','MMS') THEN type -- handle already existing strings
        ELSE 'EMAIL'
        END;

-- Step 3: Convert status column safely
ALTER TABLE notification ALTER COLUMN status TYPE VARCHAR(50) USING
    CASE
        WHEN status::TEXT = '0' THEN 'PENDING'
        WHEN status::TEXT = '1' THEN 'PROCESSING'
        WHEN status::TEXT = '2' THEN 'SUCCESS'
        WHEN status::TEXT = '3' THEN 'FAILED'
        WHEN status IN ('PENDING','PROCESSING','SUCCESS','FAILED') THEN status
        ELSE 'PENDING'
        END;

-- Step 4: Add new check constraints for enum string values
ALTER TABLE notification ADD CONSTRAINT notification_type_check
    CHECK (type IN ('EMAIL', 'SMS', 'MMS'));

ALTER TABLE notification ADD CONSTRAINT notification_status_check
    CHECK (status IN ('PENDING', 'PROCESSING', 'SUCCESS', 'FAILED'));
