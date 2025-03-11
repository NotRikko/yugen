"use client"

import {
  useForm
} from "react-hook-form"
import {
  zodResolver
} from "@hookform/resolvers/zod"
import * as z from "zod"
import {
  Button
} from "@/components/ui/button"
import {
  Form,
  FormControl,
  FormDescription,
  FormField,
  FormItem,
  FormMessage,
} from "@/components/ui/form"
import {
  Input
} from "@/components/ui/input"
import { Textarea } from "@/components/ui/textarea"


import { useUser } from "@/UserProvider";



const formSchema = z.object({
    content: z.string().max(280, "Post content must be at most 280 characters"),
    productId: z.number().optional(),
    files: z.array(z.instanceof(File)).optional()
});

export default function PostCreate() {
    const {user} = useUser();

    const form = useForm<z.infer<typeof formSchema>>({
        resolver: zodResolver(formSchema),
        defaultValues: {
            content: "",
            productId: undefined,
            files: [],
        }
    })

    async function onSubmit(values: z.infer<typeof formSchema>) {
        try {
            const formData = new FormData();
            
            const postData = {
                artistId: user.artistId,
                content: values.content,
                productId: values.productId ?? null,
            };
            formData.append("post", new Blob([JSON.stringify(postData)], { type: "application/json" }));
    
            if (values.files && values.files.length > 0) {
                values.files.forEach((file) => {
                    formData.append("files", file);
                });
            }
    
            const response = await fetch("http://localhost:8080/posts/create", {
                mode: "cors",
                method: "POST",
                body: formData,
            });
    
            if (!response.ok) {
                throw new Error("Failed to create post");
            }
            
            console.log("Post created successfully");
    
        } catch (error) {
            console.error("Error creating post", error);
        }
    }
    return (
        <Form {...form} >
            <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-8 mx-auto py-8 w-5/6 mx-auto p-4 border rounded-lg shadow-md bg-white">
                <FormField
                    control={form.control}
                    name="content"
                    render={({ field }) => (
                    <FormItem>
                        <FormControl>
                            <Textarea 
                                placeholder="What's up with your new work?"
                                className="h-20"
                                {...field} 
                            />
                        </FormControl>
                        <FormMessage />
                    </FormItem>
                    )}
                />
                <FormField
                    control={form.control}
                    name="files"
                    render={({ field }) => (
                        <FormItem>
                        <FormControl>
                            <Input
                            type="file"
                            accept="image/*"
                            multiple
                            onChange={(e) => {
                                // Ensure files are always in array format
                                const files = e.target.files ? Array.from(e.target.files) : [];
                                field.onChange(files); // Set the value as an array
                            }}
                            />
                        </FormControl>
                        <FormDescription>Upload images for your post.</FormDescription>
                        <FormMessage />
                        </FormItem>
                    )}
                />
                    
            <Button type="submit">Post</Button>
            </form>
        </Form>
    )
}