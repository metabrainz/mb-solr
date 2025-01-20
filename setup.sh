#!/bin/bash

# Arguments
MAIN_DIR="mbsssss"
UPLOAD_URL="http://localhost:8983/api/cluster/configs"
EXCLUDE_DIRS="lib common _template"

# Populate the list of subdirectories
SUBDIRS=$(find $MAIN_DIR -maxdepth 1 -mindepth 1 -type d -print0 | xargs -0 -n 1 basename)

# Exclude specified directories
if [ -n "$EXCLUDE_DIRS" ]; then
    for EXCLUDE_DIR in $EXCLUDE_DIRS; do
        SUBDIRS=$(echo "$SUBDIRS" | grep -v "^$EXCLUDE_DIR$")
    done
fi

# Iterate over the filtered subdirectories
for SUBDIR in $SUBDIRS; do
    FULL_PATH="$MAIN_DIR/$SUBDIR/conf"
    FULL_PATH=$(realpath "$FULL_PATH")

    # Check if the subdirectory exists
    if [ ! -d "$FULL_PATH" ]; then
        echo "Directory $FULL_PATH does not exist. Skipping..."
        continue
    fi

    # Change to the directory being zipped
    cd "$FULL_PATH" || { echo "Failed to change to directory $FULL_PATH. Skipping..."; continue; }

    # Create a zip file of the subdirectory
    ZIP_FILE="$SUBDIR.zip"
    echo "Zipping $FULL_PATH into $ZIP_FILE..."
    if ! zip -r "$ZIP_FILE" -- * >/dev/null; then
        echo "Error zipping $FULL_PATH. Skipping..."
        cd - >/dev/null || exit 1
        continue
    fi

    # Make the curl PUT request
    echo "Uploading $ZIP_FILE to $UPLOAD_URL..."
    if curl -X PUT --header "Content-Type:application/octet-stream" --data-binary @"$ZIP_FILE" "$UPLOAD_URL/$SUBDIR"; then
        echo ""
        echo "Upload successful. Deleting $ZIP_FILE..."
        rm "$ZIP_FILE"

        echo "Creating collection $SUBDIR..."
        curl -X POST http://localhost:8983/api/collections \
            -H 'Content-Type: application/json' \
            --data-binary "{
                \"name\": \"${SUBDIR}\",
                \"config\": \"${SUBDIR}\",
                \"numShards\": 1
            }"
        echo ""
    else
        echo ""
        echo "Upload failed for $ZIP_FILE. Keeping the file for debugging."
    fi

    # Return to the original directory
    cd - >/dev/null || exit 1

done
